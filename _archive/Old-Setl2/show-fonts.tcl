#!/usr/bin/wish
label .label -text "Dies ist ein Test für die Fonts."
pack .label
font create myfont -family chevara
foreach f [font families] { 
	font delete myfont; 
	font create myfont -size 16 -family $f;
	.label config -font myfont; 
	puts $f; 
	update;
	gets stdin
}
