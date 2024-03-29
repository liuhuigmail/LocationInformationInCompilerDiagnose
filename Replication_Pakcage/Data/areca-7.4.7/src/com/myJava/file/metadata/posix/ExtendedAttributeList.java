package com.myJava.file.metadata.posix;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.myJava.object.EqualsHelper;
import com.myJava.object.HashHelper;
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
public class ExtendedAttributeList {
	private List content;
	
	public ExtendedAttributeList() {
		content = new ArrayList();
	}
	
	public void addAttribute(String name, byte[] data) {
		content.add(new ExtendedAttribute(name, data));
	}
	
	public int size() {
		return content.size();
	}
	
	public Iterator iterator() {
		return content.iterator();
	}
	
	public boolean isEmpty() {
		return content.isEmpty();
	}
	
	public String toString() {
		StringBuffer sb1 = new StringBuffer();
		if (this == null) {
		    sb1.append("<null>");
		} else {
		    sb1.append("[").append(this.getClass().getSimpleName());
		}
		StringBuffer sb = sb1;
		ToStringHelper.append("content", content, sb);
		return ToStringHelper.close(sb);
	}
	
	public boolean equals(Object obj) {
		if (! EqualsHelper.checkClasses(obj, this)) {
			return false;
		} else {
			ExtendedAttributeList other = (ExtendedAttributeList)obj;
			return EqualsHelper.equals(other.content, this.content);
		}
	}

	public int hashCode() {
		int h = HashHelper.initHash(this);
		h = HashHelper.hash(h, content == null ? null : content.iterator());
		return h;
	}
}
