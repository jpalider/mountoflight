import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import java.lang.String;
import java.util.Vector;

import java.io.IOException;
//import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * It is a font class responsible for drawing characters and strings, either
 * freestyle or correctly formatted. 
 * 
 * @author Jakub Palider, 24 Apr 2008
 * @version 1.0
 * 
 * TODO: 
 * 		wrapping
 * 		font selection
 * 		more characters
 * 
 */

public class QFont {
	
	static final int QUESTION_MARK = 33;
	static final int EXCLAMATION_MARK = 63;
	static final int DOT = 44;
	static final int COMMA = 46;
	static final int SPACE = 32;
	
	static final int QUESTION_MARK_OFFSET = 0;
	static final int EXCLAMATION_MARK_OFFSET = 1;
	static final int DOT_OFFSET = 2;
	static final int COMMA_OFFSET = 3;
	static final int SPACE_OFFSET = 4;
	
	
	Image img;
	int height;
	int interline;
	// letter            a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, 
	int sCharWidth[];// = { 5, 7, 5, 6, 5, 4, 7, 5, 4, 4, 6, 4, 9, 5, 6, 6, 7, 4, 4, 5, 6, 6, 7, 4, 5, 5, };
	// in capital
	int lCharWidth[];// = { 7, 6, 6, 7, 6, 6, 7, 8, 5, 5, 7, 7, 9, 8, 7, 6, 9, 7, 5, 7, 9, 8, 13,9, 8, 7, };
	// extra chars: ?!.,SPACE
	int eCharWidth[];// = {6,1,2,2,5,};
	int sCharOffset[];
	int lCharOffset[];
	int eCharOffset[];
	
	QFont(Image i, String fdf){
		/*
		 */
		
		int fileSize = 0;
		InputStreamReader insr = null;
		try {
			insr = new InputStreamReader(getClass().getResourceAsStream(fdf));
			while(insr.read() != -1) {
				fileSize++;
			}
			insr.close();
		} catch (IOException e) {
			System.out.println("QFont() insr.read(cbuf) error");
		}
		char[] cbuf = new char[fileSize+1];
		try {
			insr = new InputStreamReader(getClass().getResourceAsStream(fdf));
			insr.read(cbuf);
			insr.close();
		} catch (IOException e) {
			System.out.println("QFont() insr.read(cbuf) error" + e.toString());
		}
		String str = new String(cbuf);
		cbuf = null;
		
		// all head comments are removed...
		str = str.substring(str.indexOf('\n',str.lastIndexOf('#'))+1);
		
		// get font height
		height = Integer.parseInt(str.substring(0,str.indexOf(',')));
		str = str.substring(str.indexOf('\n')+1);
		
		int[] tmpBuf = new int[256];
		
		// get char widths
		int n = 0;
		while(str.indexOf('\n')!=0){
			tmpBuf[n] = Integer.parseInt( str.substring(0,str.indexOf(',')) );
			str = str.substring(str.indexOf(',')+1);
			n++;
		}
		str = str.substring(str.indexOf('\n')+1);
		sCharWidth = new int[n];
		for (int j = 0; j < n; j++) {
			sCharWidth[j] = tmpBuf[j];
		}
		
		// get CHAR widths
		n = 0;
		while(str.indexOf('\n')!=0){
			tmpBuf[n] = Integer.parseInt( str.substring(0,str.indexOf(',')) );
			str = str.substring(str.indexOf(',')+1);
			n++;
		}		
		str = str.substring(str.indexOf('\n')+1);
		lCharWidth = new int[n];
		for (int j = 0; j < n; j++) {
			lCharWidth[j] = tmpBuf[j];
		}
		
		// get extra char widths
		n = 0;
		while(str.indexOf('\n')!=0){
			tmpBuf[n] = Integer.parseInt( str.substring(0,str.indexOf(',')) );
			str = str.substring(str.indexOf(',')+1);
			n++;
		}
		str = str.substring(str.indexOf('\n')+1);
		eCharWidth = new int[n];
		for (int j = 0; j < n; j++) {
			eCharWidth[j] = tmpBuf[j];
		}
		tmpBuf = null;
	
		img = i;
		sCharOffset = new int[sCharWidth.length];
		lCharOffset = new int[lCharWidth.length];
		eCharOffset = new int[eCharWidth.length];
		
		for (int j = 1; j < sCharWidth.length; j++) {		
			sCharOffset[j] = sCharWidth[j-1] + sCharOffset[j-1];			
		}
		
		for (int j = 1; j < lCharWidth.length; j++) {		
			lCharOffset[j] = lCharWidth[j-1] + lCharOffset[j-1];			
		}
		
		for (int j = 1; j <eCharWidth.length; j++) {		
			eCharOffset[j] = eCharWidth[j-1] + eCharOffset[j-1];			
		}
		
		interline = height>>4;
	}

	/**
	 * Draws a single character using previously defined font image.
	 * 
	 * @param g the Graphics object to be used for rendering the character
	 * @param c char to be drawn
	 * @param x the x coordinate of the font character to be drawn
	 * @param y the y coordinate of the font character to be drawn
	 */
	int drawChar(Graphics g, char c, int x, int y) {
		int offsetLine;
		int width;
		int offset;
		if ((int)c >= 97 && (int)c <= 122) {
			offsetLine = 0;
			width = sCharWidth[(int)c - 97];
			offset = sCharOffset[(int)c - 97];
		} else if ((int)c >= 65 && (int)c <=  90) {
			offsetLine = 1;
			width = lCharWidth[(int)c - 65];
			offset = lCharOffset[(int)c - 65];
		} else {
			offsetLine = 2;
			switch (c) {			
				case QUESTION_MARK:
					width = eCharWidth[QUESTION_MARK_OFFSET];
					offset = eCharOffset[QUESTION_MARK_OFFSET];
					break;
				case EXCLAMATION_MARK:
					width = eCharWidth[EXCLAMATION_MARK_OFFSET];
					offset = eCharOffset[EXCLAMATION_MARK_OFFSET];
					break;
				case DOT:
					width = eCharWidth[DOT_OFFSET];
					offset = eCharOffset[DOT_OFFSET];
					break;
				case COMMA:
					width = eCharWidth[COMMA_OFFSET];
					offset = eCharOffset[COMMA_OFFSET];
					break;
				case SPACE:
					width = eCharWidth[SPACE_OFFSET];
					offset = eCharOffset[SPACE_OFFSET];
					break;
				default:
					width = eCharWidth[1];
					offset = eCharOffset[1];
			}
		}
		
		g.setClip(x, y,width, height);
		g.drawImage(img, x-offset, y-offsetLine*height, 0);
		
		return width;
	}
	
	/**
	 * Draws a string using previously defined font image. No formatting is
	 * provided.
	 * 
	 * @param g the Graphics object to be used for rendering the string
	 * @param s string to be drawn
	 * @param x the x coordinate of the string to be drawn
	 * @param y the y coordinate of the string to be drawn
	 */
	void drawString(Graphics g, String s, int x, int y) {
		int clipX = g.getClipX();
		int clipY = g.getClipY();
		int clipW = g.getClipWidth();
		int clipH = g.getClipWidth();
		
		for (int i = 0; i < s.length(); i++) {
			x += drawChar(g,s.charAt(i),x,y);
			//x += 2;//space between letters			
		}		
		g.setClip(clipX, clipY, clipW, clipH);
	}
	
	// TODO: version with String[]
	
	int drawString(Graphics g, String s, int x, int y, int lineWidth) {
		// I just want have this stuff working... if it will turn out to be too
		// slow or buggy (not idiot/myself proof) it will be changed. I am very
		// aware of this code quality but as for now it should just work.
		// I hope to use it as a black box since now on ;-)
		
		// It is a result of changing approach - searching spaces, not words,
		// within the text 
		
		// No caching of precalculated substrings length is performed... this
		// might be a performance issue in the future. At least we know where
		// to start from!
		
		int spacePosition = 0;
		int linePosition = 0;
		Vector stringVector = new Vector();
		String line;
		int csw = 0;
		int sp = 0;

		// loop until no SPACE is found
		do {
			csw = 0;
			sp = spacePosition;
			do {				
				csw = calculateStringWidth( s.substring(linePosition, sp) );
				if (csw <= lineWidth) {
					spacePosition = sp+1; // skip SPACES
				} else {	
					break;
				}
				sp = s.indexOf(' ', spacePosition);
			} while ( sp != -1 );
			

			line = s.substring(linePosition, spacePosition);				
			linePosition = spacePosition;
			stringVector.addElement(line);
			
		} while(spacePosition < s.length() && sp != -1);
		
		//process remaining 
		
		if (lineWidth - csw >=  calculateStringWidth(s.substring(linePosition)) ) {
			line += s.substring(spacePosition);
			stringVector.setElementAt(line, stringVector.size()-1);
		} else {
			line = s.substring(spacePosition);		
			stringVector.addElement(line);
		}
		
		for(int i = 0; i < stringVector.size(); i++) {
			drawString(g, stringVector.elementAt(i).toString(), x, y);
			y += height + interline;
		}
		
		return spacePosition;
	}

	/*
	void drawString(Graphics g, String s, int x, int y, int lineWidth) {
		//int calcWidth
		String[] sstr = splitString(s, lineWidth);
		drawString(g, s, x, y);
		//version with String[] not String
	}
	*/
	
	String[] splitString(String s, int maxWidth) {
		String[] ss=null;
		return ss;
	}
	
	int calculateStringWidth(String s) {
		int w=0;
		for (int i = 0; i < s.length(); i++) {
			w += getCharWidth(s.charAt(i));
		}
		return w;
	}
	
	int getCharWidth(char c) {
		
		if ( c>=65 && c<=90) {
			return lCharWidth[c-65];
		} else if ( c>=97 &&  c<=122) {
			return sCharWidth[c-97];
		} else {
			//calculate manually by comparing the chars to known extra chars
			switch (c) {			
				case QUESTION_MARK:
					return eCharWidth[QUESTION_MARK_OFFSET];
				case EXCLAMATION_MARK:
					return eCharWidth[EXCLAMATION_MARK_OFFSET];
				case DOT:
					return eCharWidth[DOT_OFFSET];
				case COMMA:
					return eCharWidth[COMMA_OFFSET];
				case SPACE:
					return eCharWidth[SPACE_OFFSET];
				default:
					return -1;			
			}
			
		}
	}

/**
	! = 33
	" = 34
	# = 35
	$ = 36
	% = 37
	& = 38
	' = 39
	( = 40
	) = 41
	* = 42
	+ = 43
	, = 44
	- = 45
	. = 46
	/ = 47
	0 = 48
	1 = 49
	2 = 50
	3 = 51
	4 = 52
	5 = 53
	6 = 54
	7 = 55
	8 = 56
	9 = 57
	: = 58
	; = 59
	< = 60
	= = 61
	> = 62
	? = 63
	@ = 64
	A = 65
	B = 66
	C = 67
	D = 68
	E = 69
	F = 70
	G = 71
	H = 72
	I = 73
	J = 74
	K = 75
	L = 76
	M = 77
	N = 78
	O = 79
	P = 80
	Q = 81
	R = 82
	S = 83
	T = 84
	U = 85
	V = 86
	W = 87
	X = 88
	Y = 89
	Z = 90
	[ = 91
	\ = 92
	] = 93
	^ = 94
	_ = 95
	` = 96
	a = 97
	b = 98
	c = 99
	d = 100
	e = 101
	f = 102
	g = 103
	h = 104
	i = 105
	j = 106
	k = 107
	l = 108
	m = 109
	n = 110
	o = 111
	p = 112
	q = 113
	r = 114
	s = 115
	t = 116
	u = 117
	v = 118
	w = 119
	x = 120
	y = 121
	z = 122
	{ = 123
	| = 124
	} = 125
	~ = 126
 */

}


