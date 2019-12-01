package com.myJava.object;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 
 * 
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
public class HashHelper {

	private static int BASE = 29;
	
	public static int initHash(Object argument) {
		if (argument == null) {
			return 0;
		} else {
			return hash(0, argument.getClass());
		}
	}
	
	public static int hash(int result, boolean argument) {
		return hash(result, argument ? 1 : 0);
	}
	
	public static int hash(int result, int argument) {
		return hash(result, (long)argument);
	}
    
    public static int hash(int result, int[] argument) {
        if (argument == null) {
            return result;
        } else {
            int ret = result;
            for (int i=0; i<argument.length; i++) {
                ret = hash(ret, (long)argument[i]);
            }
            return ret;
        }   
    }
    
    public static int hash(int result, byte[] argument) {
        if (argument == null) {
            return result;
        } else {
            int ret = result;
            for (int i=0; i<argument.length; i++) {
                ret = hash(ret, (long)(int) argument[i]);
            }
            return ret;
        }   
    }
	
	public static int hash(int result, long argument) {
		return (int)(result + BASE * argument);
	}
	
	// The hashCode must be order-independant in the case of a Set.
    public static int hash(int result, Set argument) {
        if (argument == null) {
            return result;
        } else {
            int[] hashCodes = new int[argument.size()];
            Iterator iter = argument.iterator();
            for (int i=0; iter.hasNext(); i++) {
                hashCodes[i] = iter.next().hashCode();
            }
            Arrays.sort(hashCodes);
            if (hashCodes == null) {
			    return result;
			} else {
			    int ret = result;
			    for (int i=0; i<hashCodes.length; i++) {
			        ret = hash(ret, (long)hashCodes[i]);
			    }
			    return ret;
			}
        }
    }
    
    public static int hash(int result, Object argument) {
        if (argument == null) {
            return result;
        } else if (argument instanceof Object[]) {
            Object[] argument1 = (Object[])argument;
			if (argument1 == null) {
			    return result;
			} else {
			    int ret = result;
			    for (int i=0; i<argument1.length; i++) {
			        ret = hash(ret, argument1[i]);
			    }
			    return ret;
			}
        } else if (argument instanceof int[]) {
            return hash(result, (int[])argument);    
        } else if (argument instanceof byte[]) {
            return hash(result, (byte[])argument);              
        } else if (argument instanceof Set) {
            Set argument1 = (Set)argument;
			if (argument1 == null) {
			    return result;
			} else {
			    int[] hashCodes = new int[argument1.size()];
			    Iterator iter = argument1.iterator();
			    for (int i=0; iter.hasNext(); i++) {
			        hashCodes[i] = iter.next().hashCode();
			    }
			    Arrays.sort(hashCodes);
			    return hash(result, hashCodes);
			}
        } else if (argument instanceof List) {
            List argument1 = (List)argument;
			Iterator iter = argument1 == null ? null : argument1.iterator();
			if (iter == null) {
			    return result;
			} else {
			    int h = result;
			    while (iter.hasNext()) {
			        h = hash(h, iter.next());
			    }
			    return h;
			}            
        } else {
            return hash(result, argument.hashCode());
        }
    }
}
