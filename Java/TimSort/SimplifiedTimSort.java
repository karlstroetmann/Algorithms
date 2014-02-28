/* This is a simplified version of TimSort.  It originated from the code 
   of Josh Block that is part of Java JDK 7 in java.util.Arrays.
   For didactical purposes, we assume that we have to sort an array of Doubles.
*/

/*
 * Copyright 2009 Google Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEA *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

/**
 * A stable, adaptive, iterative mergesort that requires far fewer than
 * n lg(n) comparisons when running on partially sorted arrays, while
 * offering performance comparable to a traditional mergesort when run
 * on random arrays.  Like all proper mergesorts, this sort is stable and
 * runs O(n log n) time (worst case).  In the worst case, this sort requires
 * temporary storage space for n/2 object references; in the best case,
 * it requires only a small constant amount of space.
 *
 * This implementation was adapted from Tim Peters's list sort for
 * Python, which is described in detail here:
 *
 *   http://svn.python.org/projects/python/trunk/Objects/listsort.txt
 *
 * Tim's C code may be found here:
 *
 *   http://svn.python.org/projects/python/trunk/Objects/listobject.c
 *
 * The underlying techniques are described in this paper (and may have
 * even earlier origins):
 *
 *  "Optimistic Sorting and Information Theoretic Complexity"
 *  Peter McIlroy
 *  SODA (Fourth Annual ACM-SIAM Symposium on Discrete Algorithms),
 *  pp 467-474, Austin, Texas, 25-27 January 1993.
 *
 * While the API to this class consists solely of static methods, it is
 * (privately) instantiable; a TimSort instance holds the state of an ongoing
 * sort, assuming the input array is large enough to warrant the full-blown
 * TimSort. Small arrays are sorted in place, using a binary insertion sort.
 *
 * @author Josh Bloch
 */

import java.util.*;

public class SimplifiedTimSort extends SortingAlgorithm {
    // This is the minimum sized sequence that will be merged.  Shorter
    // sequences will be lengthened by calling binarySort.  If the entire
    // array is less than this length, no merges will be performed.
    private static final int MIN_MERGE = 32;  

    // When we get into galloping mode, we stay there until both runs win less
    // often than MIN_GALLOP consecutive times.
    private static final int MIN_GALLOP = 7;

    // A stack of pending runs yet to be merged.  Run i starts at
    // address mRunBase[i] and extends for mRunLen[i] elements.  It's always
    // true (so long as the indices are in bounds) that:
    //
    //     mRunBase[i] + mRunLen[i] == mRunBase[i + 1].
    private int   mStackSize = 0;  // number of pending runs on stack
    private int[] mRunBase;
    private int[] mRunLen;

    public SimplifiedTimSort(Double[] array) {
        super(array);
        // Allocate temp storage (which may be increased later if necessary)
        // Actually, an array of half the size would be sufficient.  But the
        // resulting code would be much longer.
        mAux = new Double[array.length];  
        // This can be done more efficiently by calculating how much stack 
        // is actually needed.
        mRunBase = new int[40];
        mRunLen  = new int[40];
    }

    // sort the array 
    public void sort() {
        int low  = 0;
        int nRemaining = mArray.length;
        if (nRemaining < 2) {
            return;  // Arrays of size 0 and 1 are always sorted
        }
        // If the array is small, just do a binary insertion sort.
        if (nRemaining < MIN_MERGE) {
            int initRunLen = countRunAndMakeAscending(low);
            binarySort(low, mArray.length, low + initRunLen);
            return;
        }
        // March over the array once, left to right, finding natural runs,
        // extending short natural runs to minRun elements, and merging runs
        // to maintain stack invariant.
        do {
            // Identify next run
            int runLen = countRunAndMakeAscending(low);
            // If run is too short, extend to min(minRun, nRemaining)
            if (runLen < MIN_MERGE) {
                int force = nRemaining <= MIN_MERGE ? nRemaining : MIN_MERGE;
                binarySort(low, low + force, low + runLen);
                runLen = force;
            }
            // Push run onto pending-run stack, and maybe merge
            pushRun(low, runLen);
            mergeCollapse();  // establish stack invariants
            low += runLen;    // Advance to find next run
            nRemaining -= runLen;
        } while (nRemaining != 0);
        // Merge all remaining runs to complete sort
        assert low == mArray.length;
        mergeForceCollapse();
        assert mStackSize == 1;
    }

    /**
     * Sorts the specified portion of the specified array using a binary
     * insertion sort.  This is the best method for sorting small numbers
     * of elements.  It requires O(n log n) compares, but O(n^2) data
     * movement in the worst case.
     *
     * If the initial part of the specified range is already sorted,
     * this method can take advantage of it: the method assumes that the
     * elements from index {@code lo}, inclusive, to {@code start},
     * exclusive are already sorted.  The elements from start are theninserted in
     * the already sorted part.
     *
     * @param low   the index of the first element in the range to be sorted
     * @param high  the index after the last element in the range to be sorted
     * @param start the index of the first element in the range that is
     *        not already known to be sorted.
     *
     * The subarray mArray[low, ..., start - 1] is known to be sorted.
     */
    private void binarySort(int low, int high, int start) 
    {
        assert low < start && start <= high;
        for (int i = start; i < high; ++i) {
            Double next  = mArray[i];
            int    left  = low;
            int    right = i;
            assert left <= right;
            while (left < right) {
                int middle = left + (right - left) / 2;
                if (next < mArray[middle]) {
                    right = middle;
                } else {
                    left = middle + 1;
                }
            }
            assert left == right;
            System.arraycopy(mArray, left, mArray, left + 1, i - left);
            mArray[left] = next;
        }
        assert isSorted(low, high): "binarySort: not sorted";
    }

    /**
     * Returns the length of the run beginning at the specified position in
     * the specified array and reverses the run if it is descending (ensuring
     * that the run will always be ascending when the method returns).
     *
     * A run is the longest ascending sequence with:
     *
     *    mArray[lo] <= mArray[lo + 1] <= mArray[lo + 2] <= ...
     *
     * or the longest descending sequence with:
     *
     *    mArray[lo] >  mArray[lo + 1] >  mArray[lo + 2] >  ...
     *
     * For its intended use in a stable mergesort, the strictness of the
     * definition of "descending" is needed so that the call can safely
     * reverse a descending sequence without violating stability.
     *
     * @param low  index of the first element in the run
     * @param high index after the last element that may be contained in the run.
              It is required that @code{lo < hi}.
     * @return  the length of the run beginning at the specified position in
     *          the specified array
     */
    private int countRunAndMakeAscending(int low)
    {
        assert low < mArray.length;
        int high    = mArray.length;
        int runHigh = low + 1;
        if (runHigh == high) {
            return 1;
        }
        // Find end of run, and reverse range if descending
        if (mArray[runHigh] < mArray[low]) {                                  // descending run
            ++runHigh;
            while (runHigh < high && mArray[runHigh] < mArray[runHigh - 1]) { // extend it
                ++runHigh;
            }
            reverseRange(low, runHigh);  // reverse it
        } else {                                                                 // ascending run
            ++runHigh;
            while (runHigh < high && mArray[runHigh - 1] <= mArray[runHigh] ) {  // extend it
                ++runHigh;
            }
        }
        assert isSorted(low, runHigh): "run not sorted: low = " + low + " high = " + runHigh;
        return runHigh - low;   // return length of actual run
    }

    /**
     * Reverse the specified range of the specified array.
     *
     * @param low  the index of    the first element in the range to be reversed
     * @param high the index after the last  element in the range to be reversed
     */
    private void reverseRange(int low, int high) {
        int l = low;
        int h = high - 1;
        while (l < h) {
            Double t  = mArray[l];
            mArray[l] = mArray[h];
            mArray[h] = t;
            ++l; --h;
        }
        assert isSorted(low, high - 1): "not sorted after reverse: low = " + low + " high = " + high;
    }

    /**
     * Pushes the specified run onto the pending-run stack.
     *
     * @param runBase index of the first element in the run
     * @param runLen  the number of elements in the run
     */
    private void pushRun(int runBase, int runLen) {
        mRunBase[mStackSize] = runBase;
        mRunLen [mStackSize] = runLen;
        mStackSize++;
    }

    /**
     * Examines the stack of runs waiting to be merged and merges adjacent runs
     * until the following stack invariants are reestablished:
     *
     *     1. runLen[i - 3] > runLen[i - 2] + runLen[i - 1]
     *     2. runLen[i - 2] > runLen[i - 1]
     *
     * This method is called each time a new run is pushed onto the stack,
     * so the invariants are guaranteed to hold for all indexes i such that 
     * i < mStackSize upon entry to the method.
     */
    private void mergeCollapse() {
        while (mStackSize > 1) {
            int n = mStackSize - 2;
            if (n > 0 && mRunLen[n-1] <= mRunLen[n] + mRunLen[n+1]) {
                if (mRunLen[n - 1] < mRunLen[n + 1]) {
                    --n;
                }
                mergeAt(n);
            } else if (mRunLen[n] <= mRunLen[n + 1]) {
                mergeAt(n);
            } else {
                break; // invariant is established
            }
        }
    }

    /**
     * Merges all runs on the stack until only one remains.  This method is
     * called once, to complete the sort.
     */
    private void mergeForceCollapse() {
        while (mStackSize > 1) {
            mergeAt(mStackSize - 2);
        }
    }

    /**
     * Merges the two runs at stack indices i and i+1.  Run i must be
     * the penultimate or antepenultimate run on the stack.  In other words,
     * i must be equal to mStackSize-2 or mStackSize-3.
     *
     * @param i stack index of the first of the two runs to merge
     */
    private void mergeAt(int i) {
        assert mStackSize >= 2;
        assert i >= 0;
        assert i == mStackSize - 2 || i == mStackSize - 3;

        int base1 = mRunBase[i];      // start of first run
        int len1  = mRunLen[i];
        int base2 = mRunBase[i + 1];  // start of second run
        int len2  = mRunLen[i + 1];
        assert len1 > 0 && len2 > 0;
        assert base1 + len1 == base2;
        // Record the length of the combined runs; if i is the 3rd-last
        // run now, also slide over the last run (which isn't involved
        // in this merge).  The current run (i+1) goes away in any case.
        mRunLen[i] = len1 + len2;
        if (i == mStackSize - 3) {
            mRunBase[i + 1] = mRunBase[i + 2];   // slide over last run
            mRunLen [i + 1] = mRunLen [i + 2];    
        }
        --mStackSize;
        // Merge remaining runs, using mAux array 
        merge(base1, len1, base2, len2);
        assert isSorted(base1, base2 + len2): "merge:" + " base1 = " + base1 +
                                                         " len1  = " + len1  +
                                                         " base2 = " + base2 +
                                                         " len2  = " + len2;
    }

    /**
     * Locates the position at which to insert the specified key x into the
     * specified sorted range. The search is done using exponential search.
     *
     * @param  x the key whose insertion point to search for
     * @param  b the index of the first element in the range
     * @param  l the length of the range; must be > 0
     * @return the int k,  0 <= k < l such that either we have
     *              mArray[b + k - 1] <= x < mArray[b + k]
     *         or
     *              k = l and 
     *              for all i in [0, ..., l - 1]: mArray[b + i] <= x 
     */
    private int gallop(Double x, int b, int l) {
        assert isSorted(b, b + l): "argument of gallop not sorted";
        assert l > 0;
        if (x < mAux[b]) {
            return 0;
        } 
        int lastK = 0;
        int k     = 1;
        // mArray[b] <= x
        // gallop right until mAux[b + lastK] <= x < mAux[b + k]
        while (k < l && mAux[b + k] <= x) {
            lastK = k;
            k     = 2 * k + 1;
            if (k < 0) {  // beware of overflow
                k = l;   
            }
        }
        if (k > l) {
            k = l;
        }
        assert 0 <= lastK && lastK < k && k <= l;
        // Now mAux[b + lastK] <= x < mAux[b + k], so x belongs somewhere to
        // the right of lastK but no farther right than k.  Do a binary
        // search, with invariant mAux[b + lastK] <= x < mAux[b + k].
        while (lastK < k) {
            int m = lastK + (k - lastK) / 2;
            assert m < l;
            if (mAux[b + m] <= x) {
                lastK = m + 1;  // mAux[b + m] <= x
            } else {
                k = m;          // x < mAux[b + m]
            }
        }
        assert lastK == k;    // so mAux[b + k - 1] <= x < mAux[b + k]
        return k;             // x belongs at offset
    }

    /**
     * Merges two adjacent runs in place, in a stable fashion.  The first
     * element of the first run must be greater than the first element of the
     * second run (mArray[b1] > mArray[b2]), and the last element of the first run
     * (mArray[b1 + l1-1]) must be greater than all elements of the second run.
     *
     * @param b1 index of first element in first run to be merged
     * @param l1  length of first run to be merged (must be > 0)
     * @param b2 index of first element in second run to be merged
     *              (must be b1 + l1)
     * @param len2  length of second run to be merged (must be > 0)
     */
    private void merge(int b1, int l1, int b2, int l2) {
        assert l1 > 0 && l2 > 0 && b1 + l1 == b2;
        System.arraycopy(mArray, b1, mAux, b1, l1);
        System.arraycopy(mArray, b2, mAux, b2, l2);
        int c1 = b1;   // indexes into first  run 
        int c2 = b2;   // indexes into second run 
        int d  = b1;   // destination, index where to write next element
    outer:
        while (true) {
            int n1 = 0; // Number of times in a row that first  run won
            int n2 = 0; // Number of times in a row that second run won
            do {
                assert l1 > 0 && l2 > 0;
                if (mAux[c2] < mAux[c1]) {
                    mArray[d] = mAux[c2];
                    ++d; ++c2; --l2;
                    if (l2 == 0) { 
                        break outer;
                    }
                    ++n2; n1 = 0;
                } else { // mArray[c1] <= mAux[c2]
                    mArray[d] = mAux[c1];
                    ++d; ++c1; --l1;
                    if (l1 == 0) { 
                        break outer;
                    }
                    n1++; n2 = 0;
                }
            } while (n1 + n2 < MIN_GALLOP);
            do {
                assert l1 > 0 && l2 > 0;
                n1 = gallop(mAux[c2], c1, l1);
                if (n1 != 0) {
                    System.arraycopy(mAux, c1, mArray, d, n1);
                    d  += n1;
                    c1 += n1;
                    l1 -= n1;
                    if (l1 == 0) { 
                        break outer;
                    }
                }
                n2 = gallop(mAux[c1], c2, l2);
                if (n2 != 0) {
                    System.arraycopy(mAux, c2, mArray, d, n2);
                    d += n2;
                    c2 += n2;
                    l2 -= n2;
                    if (l2 == 0) {
                        break outer;
                    }
                }
            } while (n1 + n2 >= MIN_GALLOP);
        }  // End of "outer" loop
        if (l1 == 0) {
            assert l2 > 0;
            System.arraycopy(mArray, c2, mArray, d, l2);
        } else {
            assert l2 == 0;
            System.arraycopy(mAux, c1, mArray, d, l1);
        }
    }
}
