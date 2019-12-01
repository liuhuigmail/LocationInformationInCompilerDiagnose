package com.application.areca.filter;
import java.io.Serializable;
import com.myJava.file.iterator.FileSystemIteratorFilter;
import com.myJava.object.Duplicable;
/* Archive filter interface.
 * <BR>
 * @author Olivier PETRUCCI
 /

public interface ArchiveFilter {
  public boolean isLogicalNot();
 /*
  * Parses the string provided as argument and inits the filter
  */
    public void acceptParameters(String parameters);
   
}