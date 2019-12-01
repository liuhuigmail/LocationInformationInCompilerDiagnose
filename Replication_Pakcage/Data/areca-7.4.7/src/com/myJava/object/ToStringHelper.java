package com.myJava.object;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Helper for "toString" methods
 * <BR>
 * @author Olivier PETRUCCI
 * <BR>
 *
 */

 /*
 Copyright 2005-2014, Olivier PETRUCCI.

This file is part of Areca.

    Areca is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    Areca is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Areca; if not, write to the Free Software
    Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 */
public class ToStringHelper {
	private static NumberFormat nf = NumberFormat.getNumberInstance();
	
	static {
		nf.setMinimumFractionDigits(3);
		nf.setMaximumFractionDigits(3);
	}
	
    public static void append(String name, Object o, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        if (o == null) {
		    sb.append("<null>");
		} else {
		    sb.append(o.toString());
		}
        postAppend(sb);
    }
    
    public static void append(String name, String s, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        if (s == null) {
		    sb.append("<null>");
		} else {
		    sb.append("\"").append(s).append("\"");
		}
        postAppend(sb);
    }
    
    public static void append(String name, Set s, StringBuffer sb) {
        if (s == null) {
            sb.append(" - ");
			sb.append(name).append("=");
			normalize(null, sb);
			postAppend(sb);
        } else {
            append(name, s.iterator(), sb);
        }
    }
    
    public static void append(String name, List l, StringBuffer sb) {
        if (l == null) {
            sb.append(" - ");
			sb.append(name).append("=");
			normalize(null, sb);
			postAppend(sb);
        } else {
            append(name, l.iterator(), sb);
        }
    }
    
    public static String serialize(Object[] o) {
        if (o == null) {
            return "<null>";
        } else {
            StringBuffer b = new StringBuffer();
            b.append('{');
            for (int i=0; i<o.length; i++) {
                if (i != 0) {
                    b.append(", ");
                }
                if (o[i] != null) {
                    b.append(String.valueOf(o[i]));
                } else {
                    b.append("<null>");
                }
            }
            b.append('}');
            return b.toString();
        }
    }
    
    private static void appendNull(String name, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        normalize(null, sb);
        postAppend(sb);
    }
    
    private static void append(String name, Iterator iter, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        if (iter == null) {
            normalize(iter, sb);
        } else {
            sb.append("{");
            boolean first = true;
            while (iter.hasNext()) {
                if (! first) {
                    sb.append(", ");
                }
                first = false;
                normalize(iter.next(), sb);   
            }
            sb.append("}");
        }
        postAppend(sb);
    }
    
    public static void append(String name, boolean b, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        sb.append(b);
        postAppend(sb);
    }
    
    public static void append(String name, int i, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        sb.append(i);
        postAppend(sb);
    }
    
    public static void append(String name, long l, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        sb.append(l);
        postAppend(sb);
    }
    
    public static void append(String name, double d, StringBuffer sb) {
        sb.append(" - ");
		sb.append(name).append("=");
        sb.append(nf.format(d));
        postAppend(sb);
    }
    
    public static String close(StringBuffer sb) {
    	String ret = sb.toString().trim();
    	return ret + "]";
    }
    
    private static void postAppend(StringBuffer sb) {
        //sb.append(" ");
    }
    
    private static void normalize(Object o, StringBuffer sb) {
        if (o == null) {
            sb.append("<null>");
        } else {
            sb.append(o.toString());
        }
    }
    
    private static void normalize(String s, StringBuffer sb) {
        if (s == null) {
            sb.append("<null>");
        } else {
            sb.append("\"").append(s).append("\"");
        }
    }
    
    public static String serialize(byte[] dt) {
        String ret = "";
        for (int i=0; i<dt.length; i++) {
            ret += " " + dt[i];
        }
        return ret;
    }
}
