#!/usr/bin/env python3
"""Prepare the e-book version of "Algorithms and Complexity" (Python edition) for pandoc.

Adapted from the e-book script of the Logic notes (Logic/Lecture-Notes-Python/ebook/prepare.py).
The script reads the LaTeX sources in the parent directory and writes a pandoc-friendly
copy to build/.  The sources of the printed book are not changed.

- Comments are removed first, so that commented-out figures are not counted.
- Figures that pandoc cannot read (programs annotated with subscripts) are rendered with
  pdflatex and included as PNG images.
- \\epsfig is replaced by \\includegraphics; EPS and PDF images are converted to PNG.
- Program listings get their line numbers as text: the text refers to them, and e-book
  readers do not reliably support the CSS counters that pandoc uses for numbered lines.
- "on page \\pageref{...}" is removed, since an e-book has no page numbers.
- Exercises, tables, and equations are numbered here, since pandoc does not evaluate counters.
"""
import re
import shutil
import subprocess
import textwrap
from pathlib import Path

HERE     = Path(__file__).resolve().parent
SRC      = HERE.parent
BUILD    = HERE / 'build'
MASTER   = 'algorithms.tex'
CHAPTERS = ['introduction', 'python', 'complexity', 'recurrence-equations', 'correctness', 'sorting',
            'adt', 'map', 'map-avl', 'prioqueue', 'huffman', 'graph', 'monte-carlo']
TEXBIN   = '/Library/TeX/texbin'
DPI      = 250    # resolution of the figures relative to their printed size
MAX_PX   = 1600   # maximal width of a figure in pixels

def run(*cmd, cwd=BUILD):
    subprocess.run(cmd, cwd=cwd, check=True, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)

def preamble():
    s = open(SRC / MASTER, encoding='utf8').read()
    return s[:s.index('\\begin{document}')]

# ---------------------------------------------------------------------------------------------
# helpers for parsing LaTeX

def matching_brace(s, i):
    """The index of the brace that closes the brace at index i."""
    depth = 0
    while True:
        c = s[i]
        if c == '\\':
            i += 2
            continue
        depth += {'{': 1, '}': -1}.get(c, 0)
        if depth == 0:
            return i
        i += 1

def unwrap(s, cmd, skip=0):
    """Replace \\cmd{a_1}...{a_skip}{content} (optional arguments anywhere) by content."""
    pattern = re.compile(r'\\' + cmd + r'(?![a-zA-Z])\*?')
    while (m := pattern.search(s)):
        j, args = m.end(), []
        while len(args) <= skip:
            while j < len(s) and s[j] in ' \t\n':
                j += 1
            if s[j] == '[':
                j = s.index(']', j) + 1
            elif s[j] == '{':
                end = matching_brace(s, j)
                args.append(s[j + 1:end])
                j = end + 1
            else:
                break
        content = args[skip] if len(args) > skip else ''
        s = s[:m.start()] + content + s[j:]
    return s

VERBATIM = r'\\begin\{(minted|python3code|Verbatim|verbatim|lstlisting)\}.*?\\end\{\1\}'

def strip_comments(s):
    """Remove comments as TeX does (including the end of the line and the indentation of the
    next line), but not inside listings, URLs, and \\verb."""
    def strip(text):
        protected = []
        def protect(m):
            protected.append(m.group(0))
            return f'\x00{len(protected) - 1}\x00'
        text = re.sub(r'\\(?:href|url)\{[^}]*\}', protect, text)
        text = re.sub(r'\\verb(.).*?\1', protect, text)
        text = re.sub(r'(?<!\\)%[^\n]*(\n[ \t]*)?', '', text)
        return re.sub(r'\x00(\d+)\x00', lambda m: protected[int(m.group(1))], text)
    out, pos = [], 0
    for m in re.finditer(VERBATIM, s, re.S):
        out += [strip(s[pos:m.start()]), m.group(0)]
        pos = m.end()
    return ''.join(out) + strip(s[pos:])

# ---------------------------------------------------------------------------------------------
# macros

def macros():
    """The macro definitions of the preamble, adapted for an e-book."""
    # take the complete \newcommand blocks (some span several lines) via a brace count
    s, out = preamble(), []
    for m in re.finditer(r'\\(?:re)?newcommand\{\\(\w+)\}(\[\d\])?\{', s):
        out.append(s[m.start():matching_brace(s, m.end() - 1) + 1])
    replaced = ('textsc|chaptermark|sectionmark|exercise|ex|proof|solution|example|examples|remark|hint|'
                'green|orange|blue|red|magenta|mycheck|dv|dvv|bruch|head|tail|source|target|mmod|mdiv|'
                'conclude|qed|qeds|eox|eoxs|lb|mytt|squote')
    out = [d for d in out if not re.match(r'\\(?:re)?newcommand\{\\(' + replaced + r')\}', d)]
    # colors are not supported in pandoc's math, page layout does not exist in an e-book
    out += [r'\newcommand{\mytt}[1]{\texttt{#1}}',
            r'\newcommand{\blue}[1]{\emph{#1}}',
            r'\newcommand{\red}[1]{#1}',
            r'\newcommand{\green}[1]{#1}',
            r'\newcommand{\orange}[1]{#1}',
            r'\newcommand{\magenta}[1]{\textbf{#1}}',
            r'\newcommand{\bruch}[2]{\dfrac{#1}{#2}}',
            r'\newcommand{\dv}{\mathbin{\texttt{//}}}',
            r'\newcommand{\dvv}{\mathbin{\texttt{//}}}',
            r'\newcommand{\mdiv}{\mathbin{\texttt{//}}}',
            r'\newcommand{\mmod}{\mathbin{\texttt{\%}}}',
            r'\newcommand{\head}{\mathtt{head}}',
            r'\newcommand{\tail}{\mathtt{tail}}',
            r'\newcommand{\source}{\mathtt{source}}',
            r'\newcommand{\target}{\mathtt{target}}']
    out += re.findall(r'\\newtheorem\{[^}]*\}(?:\[[^\]]*\])?\{[^}]*\}', s)
    return simplify('\n'.join(out))

def simplify(s):
    """Replace constructs that pandoc's LaTeX reader or its math parser do not know."""
    s = s.replace('\\textsl{', '\\textit{')
    s = re.sub(r'\\hspace\*?\{-[^}]*\}', '', s)
    s = re.sub(r'\\hspace\*?\{[^}]*\}', r'\\quad ', s)
    s = re.sub(r'\\begin\{array\}\[\w\]', r'\\begin{array}', s)
    s = re.sub(r'\\index\{(?:[^{}]|\{(?:[^{}]|\{[^{}]*\})*\})*\}', '', s)
    s = s.replace('\\symbol{92}', '\\textbackslash ').replace('\\symbol{36}', '\\$')
    s = s.replace('\\symbol{34}', '"').replace('\\symbol{37}', '\\%').replace('\\symbol{126}', '\\~{}')
    s = re.sub(r'\\\\\[[^\]]*\]', r'\\\\', s)
    s = re.sub(r'\\color\{\w+\}\{\\surd\}|\\surd', r'\\checkmark', s)
    s = s.replace('\\bigm', '\\big').replace('\\Bigm', '\\Big')
    s = re.sub(r'\\rule(\[[^\]]*\])?\{[^}]*\}\{[^}]*\}', '', s)
    s = s.replace('\\mbox{\\scriptsize def}', '\\text{def}')
    # inside math: no colors
    def plain(p):
        p = re.sub(r'\\(blue|red|green|orange)\{', '{', p)
        return re.sub(r'\\textcolor\{\w+\}\{', '{', p)
    s = re.sub(r'\\begin\{(array|eqnarray\*?|equation\*?|displaymath)\}.*?\\end\{\1\}',
               lambda m: plain(m.group(0)), s, flags=re.S)
    parts = re.split(r'(?<!\\)\$', s)
    for i in range(1, len(parts), 2):
        parts[i] = plain(parts[i])
    return '$'.join(parts)

def text_macros(s):
    """The macros of the notes that typeset headings like "Proof:" or need vertical space."""
    s = re.sub(r'\\textsc\{([^{}]*)\}', lambda m: m.group(1).upper(), s)
    for name, word in [('examples', 'Examples'), ('example', 'Example'), ('proof', 'Proof'),
                       ('solution', 'Solution'), ('remark', 'Remark'), ('hint', 'Hint')]:
        s = re.sub(r'\\' + name + r'(?![a-zA-Z])\s*', f'\n\n\\\\textbf{{{word}}}: ', s)
    s = re.sub(r'\\ex(?![a-zA-Z])', '', s)
    s = re.sub(r'\\(?:qeds?|conclude)(?![a-zA-Z])', ' □', s)
    s = re.sub(r'\\eoxs?(?![a-zA-Z])', ' ◇', s)
    s = re.sub(r'\\mycheck(?![a-zA-Z])', ' ✓', s)
    s = re.sub(r'\\(?:lb|FloatBarrier|pagebreak|newpage|clearpage|noindent|linebreak)(?![a-zA-Z])', '', s)
    s = re.sub(r'\\vspace\*?\{[^}]*\}', '', s)
    s = re.sub(r'\\d(?:Changey|Vomey)(?:\[[^\]]*\])*\{[^}]*\}', '', s)
    # underlining tricks that avoid the descenders of g and y
    s = s.replace('\\underline{an}y\\underline{\\ }', '\\underline{any}')
    s = s.replace('\\underline{avera}g\\underline{e}', '\\underline{average}')
    s = s.replace('g\\underline{uaranteed}', '\\underline{guaranteed}')
    s = s.replace('\\mbox{p\\hspace{-0.15cm}\\underline{\\hspace{0.15cm}airs}}', '\\underline{pairs}')
    s = re.sub(r'\\mbox\{\\,\\mytt\{/\}\$\\!\$\\mytt\{/\}\\,\}', r'\\mathbin{\\texttt{//}}', s)
    for cmd, skip in [('colorbox', 1), ('textcolor', 1), ('framebox', 0), ('fbox', 0), ('raisebox', 1)]:
        s = unwrap(s, cmd, skip)
    return s

def labelled_lists(s):
    """pandoc drops the labels of \\item[B.C.:] in an enumerate list, so such lists become
    description lists."""
    out, pos = [], 0
    for m in re.finditer(r'\\begin\{enumerate\}(\[[^\]]*\])?', s):
        if m.start() < pos:
            continue
        # find the matching \end{enumerate}
        depth, j = 1, m.end()
        while depth:
            n = re.compile(r'\\(begin|end)\{enumerate\}').search(s, j)
            depth += 1 if n.group(1) == 'begin' else -1
            j = n.end()
        body = s[m.end():j - len('\\end{enumerate}')]
        first = re.search(r'\\item\b(\[)?', body)
        if first and first.group(1):
            out += [s[pos:m.start()], '\\begin{description}', body, '\\end{description}']
            pos = j
    return ''.join(out) + s[pos:]

# ---------------------------------------------------------------------------------------------
# listings and figures

class Listings:
    """Python listings with their line numbers as text; firstnumber = last continues the
    numbering of the previous listing, as in minted."""
    last = 0
    def __call__(self, m):
        env, options, code = m.group(1), m.group(2) or '', m.group(4)
        lines = textwrap.dedent(code).rstrip().split('\n')
        numbered = env == 'python3code' or re.search(r'numbers\s*=\s*left', options)
        if numbered:
            first = re.search(r'firstnumber\s*=\s*(\w+)', options)
            start = 1 if not first else self.last + 1 if first.group(1) == 'last' else int(first.group(1))
            self.last = start + len(lines) - 1
            width = len(str(self.last))
            lines = [f'{start + k:>{width}}  {line}'.rstrip() for k, line in enumerate(lines)]
        return '\\begin{minted}{python}\n' + '\n'.join(lines) + '\n\\end{minted}'

LISTING = (r'\\begin\{(minted|python3code)\}[ \t]*(\[[^\]]*\])?[ \t]*(\{\w+\})?[ \t]*\n'
           r'(.*?)\\end\{\1\}')

def render_figure(name, body):
    """Render the LaTeX code body as a cropped PNG image with the preamble of the notes."""
    tex = BUILD / f'{name}.tex'
    tex.write_text(preamble() + '\\pagestyle{empty}\n\\begin{document}\n' + body
                   + '\n\\end{document}\n', encoding='utf8')
    run(f'{TEXBIN}/pdflatex', '-interaction=nonstopmode', '-shell-escape', tex.name)
    run(f'{TEXBIN}/pdfcrop', f'{name}.pdf', f'{name}-crop.pdf')
    run('pdftocairo', '-png', '-r', '200', '-singlefile', f'{name}-crop.pdf', f'Figures/{name}')
    return f'Figures/{name}.png'

def pdf_width(pdf):
    info = subprocess.run(['pdfinfo', str(pdf)], capture_output=True, text=True).stdout
    return float(re.search(r'Page size:\s+([\d.]+)', info).group(1))

def image(name, scale):
    """Convert the figure name (as given to \\epsfig) to a PNG file in build/Figures."""
    path = SRC / name
    candidates = [path] if path.suffix else [path.with_suffix(e) for e in ('.pdf', '.png', '.jpg', '.eps')]
    src = next(c for c in candidates if c.exists())
    png = BUILD / 'Figures' / (src.stem + '.png')
    if src.suffix in ('.png', '.jpg'):
        png = png.with_suffix(src.suffix)
        shutil.copy(src, png)
    else:
        pdf = src
        if src.suffix == '.eps':
            pdf = BUILD / (src.stem + '.pdf')
            run(f'{TEXBIN}/epstopdf', str(src), f'--outfile={pdf}')
        width = min(MAX_PX, round(pdf_width(pdf) * scale / 72 * DPI))
        run('pdftocairo', '-png', '-scale-to-x', str(width), '-scale-to-y', '-1', '-singlefile',
            str(pdf), str(png.with_suffix('')))
    return f'Figures/{png.name}'

def epsfig_to_includegraphics(s):
    def repl(m):
        scale = re.search(r'scale\s*=\s*([\d.]+)', m.group(2))
        return f'\\includegraphics{{{image(m.group(1).strip(), float(scale.group(1)) if scale else 1.0)}}}'
    return re.sub(r'\\epsfig\{\s*file=([^,}]+)([^}]*)\}', repl, s)

def needs_image(fig):
    """Annotated programs (Verbatim with commandchars) whose variables carry subscripts
    like r$_{n+1}$ cannot be converted by pandoc."""
    return 'minted' not in fig and 'commandchars' in fig and '$_' in fig

def plain_verbatim(s):
    """Verbatim blocks with commandchars but without subscripts become plain code blocks:
    the sepia boxes and colors are removed."""
    def repl(m):
        block = m.group(0)
        if 'commandchars' not in block or '$_' in block:
            return block
        lines = re.sub(r'^\\begin\{Verbatim\}\[.*?\]\s*\n', '', block, flags=re.S).splitlines()[:-1]
        out = []
        for line in lines:
            line = re.sub(r'\\colorbox\{\w+\}\{(.*)\}\s*$', r'\1', line)
            line = re.sub(r'\\(?:green|blue|red)\{([^{}]*)\}', r'\1', line)
            out.append(line.rstrip())
        return '\\begin{Verbatim}\n' + textwrap.dedent('\n'.join(out)) + '\n\\end{Verbatim}'
    return re.sub(r'\\begin\{Verbatim\}.*?\\end\{Verbatim\}', repl, s, flags=re.S)

def special_figures(s, chapter):
    """Replace the bodies of figures that pandoc cannot convert by rendered images."""
    count = 0
    def repl(m):
        nonlocal count
        fig = m.group(0)
        if not needs_image(fig):
            return fig
        count += 1
        caption = re.search(r'\\caption\{.*?\}\s*\\label\{[^}]*\}', fig, re.S).group(0)
        body = fig[fig.index('\n'):fig.index('\\caption')]
        png = render_figure(f'{chapter}-diagram-{count}', body)
        return f'\\begin{{figure}}\n\\centering\n\\includegraphics{{{png}}}\n{caption}\n\\end{{figure}}'
    return re.sub(r'\\begin\{figure\}.*?\\end\{figure\}', repl, s, flags=re.S)

def number_floats(s, chapter_no, counters, literal_refs):
    """Put the numbers into the captions of figures and tables, and tag numbered equations.

    pandoc numbers the references to figures (per chapter, as LaTeX does) but not the captions.
    Tables and equations are not numbered at all, so references to them are replaced by their
    numbers (collected in literal_refs).  The rows of an eqnarray share the equation counter."""
    def number(env):
        counters[env] += 1
        return f'{chapter_no}.{counters[env]}'
    def repl(m):
        env, text = m.group(1), m.group(0)
        if env == 'eqnarray':
            rows = re.split(r'(\\\\)', text[len('\\begin{eqnarray}'):-len('\\end{eqnarray}')])
            for k in range(0, len(rows), 2):
                if not rows[k].strip() or '\\nonumber' in rows[k]:
                    continue
                n = number('equation')
                label = re.search(r'\\label\{([^}]*)\}', rows[k])
                if label:
                    literal_refs[label.group(1)] = n
                rows[k] = re.sub(r'\\label\{[^}]*\}', '', rows[k]).rstrip() + f' \\qquad ({n})\n'
            return '\\begin{eqnarray}' + ''.join(rows) + '\\end{eqnarray}'
        n = number(env)
        label = re.search(r'\\label\{([^}]*)\}', text)
        if env == 'equation':
            if label:
                literal_refs[label.group(1)] = n
                return re.sub(r'\\label\{[^}]*\}', f'\\\\tag{{{n}}}', text)
            return text.replace('\\end{equation}', f'\\tag{{{n}}}\\end{{equation}}')
        if env == 'table' and label:
            literal_refs[label.group(1)] = n
        return text.replace('\\caption{', f'\\caption{{{env.capitalize()} {n}: ', 1)
    return re.sub(r'\\begin\{(figure|table|equation|eqnarray)\}.*?\\end\{\1\}', repl, s, flags=re.S)

def copyright_page():
    """The copyright page of algorithms.tex up to the paragraph about the sources, which is
    written here without the centering."""
    s = open(SRC / MASTER, encoding='utf8').read()
    start = s.index('Copyright \\copyright')
    end = s.index('The sources of these lecture notes', start)
    text = re.sub(r'\n\\\\\[0\.3cm\]\n', '\n\n', s[start:end])
    return ('\n\\chapter*{Copyright}\n' + text
            + 'The sources of these lecture notes and the programs are available at\n'
            + '\\href{https://github.com/karlstroetmann/Algorithms}'
            + '{\\texttt{https://github.com/karlstroetmann/Algorithms}}.\n')

class ExerciseCounter:
    n = 0
    def __call__(self, m):
        self.n += 1
        return f'\n\n\\textbf{{Exercise {self.n}}}: '

def main():
    if BUILD.exists():
        shutil.rmtree(BUILD)
    (BUILD / 'Figures').mkdir(parents=True)
    # cs.bib may contain an empty template entry "@Book{," that bibtex skips but pandoc rejects
    bib = open(SRC / 'cs.bib', encoding='utf8').read()
    entries = re.split(r'\n(?=@)', bib)
    (BUILD / 'refs.bib').write_text('\n'.join(e for e in entries if not re.match(r'@\w+\{\s*,', e)),
                                    encoding='utf8')
    exercise, listings = ExerciseCounter(), Listings()
    literal_refs, counters, chapter_no = {}, {}, 0
    parts = [copyright_page()]
    for chapter in CHAPTERS:
        s = strip_comments(open(SRC / f'{chapter}.tex', encoding='utf8').read())
        if '\\chapter{' in s:      # map-avl.tex continues the chapter of map.tex
            chapter_no += 1
            counters = {'figure': 0, 'table': 0, 'equation': 0}
        s = special_figures(s, chapter)
        s = plain_verbatim(s)
        s = re.sub(LISTING, listings, s, flags=re.S)
        s = epsfig_to_includegraphics(s)
        s = text_macros(s)
        s = number_floats(s, chapter_no, counters, literal_refs)
        # pandoc ignores labels inside section titles
        s = re.sub(r'\\(chapter|(?:sub)*section)\{(.*?)\s*\\label\{([^}]*)\}\s*\}', r'\\\1{\2}\\label{\3}', s)
        s = re.sub(r'\s+on\s+page(?:~|\s+)\\pageref\{[^}]*\}', '', s)
        s = re.sub(r'\\exercise(?![a-zA-Z])\s*', exercise, s)
        s = re.sub(r'\\pair\(([^,()]*),([^()]*)\)', r'\\langle \1, \2 \\rangle', s)
        s = labelled_lists(simplify(s))
        # pandoc cannot parse lists inside inline font commands
        s = re.sub(r'\\textit\{\s*(\\begin\{(itemize|enumerate)\}.*?\\end\{\2\})\s*\}', r'\1', s, flags=re.S)
        parts.append(s)
    body = '\n'.join(parts)
    for label, number in literal_refs.items():
        body = body.replace(f'\\ref{{{label}}}', number)
    (BUILD / 'book.tex').write_text('\\documentclass{report}\n' + macros()
                                    + '\n\\begin{document}\n' + body + '\n\\end{document}\n',
                                    encoding='utf8')
    print(f'{exercise.n} exercises, {chapter_no} chapters, build/book.tex written')

if __name__ == '__main__':
    main()
