package com.myJava.file.delta.sequence;

import com.myJava.object.ToStringHelper;

/**
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
public class HashSequenceEntry {
    public static final byte DEFAULT_BYTE = 79;
    
    private int quickHash;
    private long index;
    private int size;
    private byte[] fullHash;
    
    public HashSequenceEntry(int quickHash, byte[] fullHash, long index, int size) {
        this.quickHash = quickHash;
        this.fullHash = fullHash;
        this.index = index;
        this.size = size;
    }
    
    public byte[] getFullHash() {
        return fullHash;
    }
    public void setFullHash(byte[] fullHash) {
        this.fullHash = fullHash;
    }
    public int getQuickHash() {
        return quickHash;
    }
    public void setQuickHash(int quickHash) {
        this.quickHash = quickHash;
    }
    public long getIndex() {
        return index;
    }
    public int getSize() {
        return size;
    }
    public void setIndex(long index) {
        this.index = index;
    }

    public String toString() {
        StringBuffer sb1 = new StringBuffer();
		if (this == null) {
		    sb1.append("<null>");
		} else {
		    sb1.append("[").append(this.getClass().getSimpleName());
		}
		StringBuffer sb = sb1;
        ToStringHelper.append("Q", quickHash, sb);
        ToStringHelper.append("F", fullHash.length, sb);
        ToStringHelper.append("P", index, sb);
        ToStringHelper.append("S", size, sb);
        return ToStringHelper.close(sb);
    }
}
