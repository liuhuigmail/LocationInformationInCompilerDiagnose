package datag.handlers;
import java.util.ArrayList;
import datag.handlers.Generation;
public class Rule {
	public int situation(String code, Generation.CH C, String res, String res2, int line)
	{
		int flag=rule_0(res,res2,line);
		if(flag!=0) return flag;
		if(C.c2.contains("{")||C.c2.contains("}")||C.c3.contains("}")||C.c3.contains("{"))
		{
			return rule_1(code,C,res,res2,line);
		}
		if(C.c2.contains("(")||C.c2.contains(")")||C.c3.contains("(")||C.c3.contains(")"))
		{
			return rule_1_1(code,C,res,res2,line);
		}
		String rule2;
		String rule22;
		rule2=search_String(code,p2line(code,C.p));
		rule22=search_String(C.code,p2line(code,C.p));
		if(C.c2.contains("/*")||C.c2.contains("*/")||C.c2.contains("//")||rule2.contains("/*")||rule2.contains("*/")||rule2.contains("//")||rule22.contains("*/")||rule22.contains("//")||rule22.contains("/*"))
		{
			return rule_2(code,C,res,res2,line);
		}
		if(tp_1(code,C,res,res2,line)>0)return tp_1(code,C,res,res2,line);
		if(C.c2.contains("for")) {if(rule_10(code,C,res,res2,line)!=0)return rule_10(code,C,res,res2,line);}
		if(C.c2.contains("catch")) {if(rule_9(code,C,res,res2,line)!=0)return rule_9(code,C,res,res2,line);}
		if(C.c2.contains("try")){if(rule_8(code,C,res,res2,line)!=0)return rule_8(code,C,res,res2,line);else if(tp_2(code,C,res,res2,line)!=0)return tp_2(code,C,res,res2,line);}
		if(rule_7(code,C,res,res2,line)!=0)return rule_7(code,C,res,res2,line);
		if(rule_11(code,C,res,res2,line)!=0)return rule_11(code,C,res,res2,line);
		if(tp_4(code,C,res,res2,line)!=0)return tp_4(code,C,res,res2,line);
		if(tp_6(code,C,res,res2,line)!=0)return tp_6(code,C,res,res2,line);
		if(tp_7(code,C,res,res2,line)!=0)return tp_7(code,C,res,res2,line);
		if(tp_8(code,C,res,res2,line)!=0)return tp_8(code,C,res,res2,line);
		if(tp_9(code,C,res,res2,line)!=0)return tp_9(code,C,res,res2,line);
		if(tp_10(code,C,res,res2,line)!=0)return tp_10(code,C,res,res2,line);
		if(tp_11(code,C,res,res2,line)!=0)return tp_11(code,C,res,res2,line);
		if(search_String(code,p2line(code,C.p)).contains("return")){if(rule_5(code,C,res,res2,line)!=0)return rule_5(code,C,res,res2,line);if(tp_3(code,C,res,res2,line)!=0)return tp_3(code,C,res,res2,line);}
		if(C.c3.contains("void")) {if(rule_6(code,C,res,res2,line)>0)return rule_6(code,C,res,res2,line);}
		if(rule_4(code,C,res,res2,line)>0)return rule_4(code,C,res,res2,line);
		if(rule_12(code,C,res,res2,line)!=0)return rule_12(code,C,res,res2,line);
		if(rule_13(code,C,res,res2,line)!=0)return rule_13(code,C,res,res2,line);
		if(rule_14(code,C,res,res2,line)!=0)return rule_14(code,C,res,res2,line);
		if(rule_15(code,C,res,res2,line)!=0)return rule_15(code,C,res,res2,line);
		if(rule_16(code,C,res,res2,line)!=0)return rule_16(code,C,res,res2,line);
		return rule_3(code,C,res,res2,line);
	}
	public String search_String(String code,int line)//提取某行的内容
	{
		int i1=0;
		int i2=0;
		int l=1;
		String x="";
		for (int i=0;i<code.length();i++)
		{
			if(l==line)
			{
				i1=i;
				while(i<code.length()-1&&code.toCharArray()[i]!='\n')
					i++;
				i2=i;
				return code.substring(i1, i2);
			}
			if(code.toCharArray()[i]=='\n')
				l++;
		}
		return x;
	}
	public boolean compare_line(ArrayList<Integer> list1,ArrayList<Integer> list2)
	{
		int i=0;
		for(;i<list1.size();i++)
		{
			if(list2.contains(list1.get(i)))
			{
				return true;
			}
				
		}
		return false;
	}
	public boolean compare_line2(ArrayList<Integer> list1,ArrayList<Integer> list2)
	{
		int i=0;
		for(;i<list1.size();i++)
		{
			if(!list2.contains(list1.get(i)))
			{
				return false;
			}
				
		}
		return true;
	}
	public ArrayList<Integer> search_line1(String code)//报告里面到底有哪些ERROR行
	{
		ArrayList<Integer> list=new ArrayList();
		String[] items=code.split("----------");
		for(String item:items) if(item.contains("ERROR in")||item.contains("WARNING in")){
			int  p=item.indexOf("(at line ");
			p+=9;
			int res=0;
			while(item.charAt(p)!=')') {
				res=res*10+item.charAt(p)-'0';
				p++;
			}
			list.add(res);
			if(item.contains("expected after this token"))
				list.add(res+1);
		}	
		return list;
	}
	public ArrayList<Integer> search_line2(String code2)//报告里面到底有哪些ERROR行
	{
		ArrayList<Integer> list=new ArrayList();
		String[] items2=code2.split(".java:");
		for(String item2:items2)if(item2.contains("error:")){
			int p=0;
			int res=0;
			while(item2.charAt(p)<='9'&&item2.charAt(p)>='0')
			{
				res=res*10+item2.charAt(p)-'0';
				p++;
			}
			if(!list.contains(res))
			{
				list.add(res);
//				System.out.println("javac:"+res);
			}
		}
		return list;
	}
	public ArrayList<Integer> search_line(String code,String code2)//报告里面到底有哪些ERROR行
	{
		ArrayList<Integer> list=new ArrayList();
		String[] items=code.split("----------");
		for(String item:items) if(item.contains("ERROR in")||item.contains("WARNING in")){
			int  p=item.indexOf("(at line ");
			p+=9;
			int res=0;
			while(item.charAt(p)!=')') {
				res=res*10+item.charAt(p)-'0';
				p++;
			}
			list.add(res);
			if(item.contains("expected after this token"))
				list.add(res+1);
		}
		String[] items2=code2.split(".java:");
		for(String item2:items2)if(item2.contains("错误")){
			int p=0;
			int res=0;
			while(item2.charAt(p)<='9'&&item2.charAt(p)>='0')
			{
				res=res*10+item2.charAt(p)-'0';
				p++;
			}
			if(!list.contains(res))
			{
				list.add(res);
//				System.out.println("javac:"+res);
			}
		}
		return list;
	}
	int p2line(String code,int position)//位置变成行
	{
		int line=1;
		int i=0;
		while(i<=position)
		{
			if(code.toCharArray()[i]=='\n')
				line++;
			i++;
		}
		return line;
	}
	public String search_word(String code,int r)//找词
	{
		if(!word(code.toCharArray()[r]))
		{
			if (r+1>=code.length())
				return code.substring(r-1);
			else
				return code.substring(r,r+1);
				
		}
		int i1=r;
		int i2=r+1;
		while (word(code.toCharArray()[i1]))
		{
			i1--;
			if (i1<0)break;
		}
		while (word(code.toCharArray()[i2])&&i2<code.length()-1)
		{
			i2++;
		}
		String x="";
		if (i2>code.length()-1)i2=code.length()-1;
		if (i1<-1)i1=-1;
		if(i2<i1+1)i1=i2-1;
		x=code.substring(i1+1,i2);
		return x;
	}
	public boolean word(char x)
	{
		if(x<='z'&&x>='a'||x>='A'&&x<='Z'||x>='0'&&x<='9')
			return true;
		else
			return false;
	}
	public int rule_16(String code,Generation.CH C, String res,String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);
		if(C.c2.contains("final")&&res.toString().contains("non-final"))
		{
			return 16;
		}
			
		return 0;
	}
	public int rule_15(String code,Generation.CH C, String res,String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);
		if(res.toString().contains("is undefined for the type "+C.c3))
		{
			return 15;
		}
			
		return 0;
	}
	public int rule_14(String code,Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);
		if(C.c2.contains("static"))
		{
			if(res.toString().contains("a static reference to the non-static"))
				return 14;
		}
			
		return 0;
	}
	public int rule_13(String code,Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);
		if(C.c2.contains("finally"))
		{
			for(int i=0;i<list.size();i++)
			{
				if(list.get(i)==line-1)
					return 13;
			}
		}
			
		return 0;
	}
	public int rule_12(String code,Generation.CH C, String res, String res2, int line)//下一个词的存在
	{
		String x="";
		int i=C.p;
		while(i<code.length()-1&&!word(code.toCharArray()[i]))
			i++;
		x=search_word(code,i);
		String[] items=res.split("----------");
		for(String item:items) if(item.contains("ERROR in")){
			int  p=item.indexOf("(at line ");
			p+=9;
			int r=0;
			while(item.charAt(p)!=')') {
				r=r*10+item.charAt(p)-'0';
				p++;
			}
			if (item.contains(x))
			{
				return 12;
			}
		}
		return 0;
	}
	public int rule_11(String code,Generation.CH C, String res, String res2, int line)
	{
		String x=search_word(code,C.p);
		String[] items=res.split("----------");
		for(String item:items) if(item.contains("ERROR in")){
			int  p=item.indexOf("(at line ");
			p+=9;
			int r=0;
			while(item.charAt(p)!=')') {
				r=r*10+item.charAt(p)-'0';
				p++;
			}
			if (item.contains("implement the inherited abstract method")&&item.contains(x))
			{
				return 11;
			}
		}
		return 0;
	}
	public int rule_10(String code,Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		for (int i=0;i<list.size();i++)
		{
			if (list.get(i)==line-1)
				return 10;
		}
		return 0;
	}
	public int rule_9(String code,Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		for (int i=0;i<list.size();i++)
		{
			String x=search_String(code,list.get(i));
			if (x.contains("}"))
				return 9;
		}
		return 0;
	}
	public int rule_8(String code,Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		for (int i=0;i<list.size();i++)
		{
			String x=search_String(code,list.get(i));
			if (x.contains("catch")||x.contains("finally"))
				return 8;
		}
		return 0;
	}
	public int rule_7(String code,Generation.CH C, String res, String res2, int line)
	{
		String x=search_String(code,line);
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行
		if(x.contains("package")||x.contains("import"))
		{
//			System.out.println("RULE7");
			for (int i=0;i<list.size();i++)
			{
				if(C.name==null)break;
				for(int j=0;j<C.name.length;j++)
				{
					if(search_String(code,list.get(i)).contains(C.name[j]))
					{
//						System.out.println("RULE7:7");
						return 7;
					}
				}				
			}
//			System.out.println("RULE7:-7");
			return 98;
		}
		return 0;
	}
	public int rule_6(String code,Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		for (int i=0;i<list.size();i++)
		{
			String x=search_String(code,list.get(i));
			if (x.contains("return"))
				return 6;
		}
		return 0;
	}
	public int rule_5(String code,Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);
//		if(res.toString().contains("This method must return a result of type"))
		if(res.toString().contains("return"))
			return 5;
		int i=-1;
		int k=C.p;
		while(k>0)
		{
			if(code.toCharArray()[k]=='}')
				i--;
			else if((code.toCharArray()[k]=='{'))
				i++;
			if (i==0)
				break;
			k--;
		}
		int i1=k;
		int flag=0;
		while (k>0)
		{
			k--;
			if(code.toCharArray()[k]=='\n')
				if(flag==0)
					continue;
				else
					break;
			if(code.toCharArray()[k]>='A'&&code.toCharArray()[k]<='Z'||code.toCharArray()[k]>='a'&&code.toCharArray()[k]<='z')
				flag=1;
		}
		int i2=k;
		String x="";
		if(i2>i1&&i1>=0)
			x=code.substring(i2,i1);
		for (int i3=0;i3<list.size();i3++)
		{
			String x2=search_String(code,list.get(i3));
			if (x2.contains(x))
			{
//				System.out.println("RULE5:5");
				return 5;
			}
		}
//		System.out.println("RULE5:-5");
		return -5;
	}
	public int rule_4(String code,Generation.CH C, String res, String res2, int line)
	{
//		System.out.println("RULE4");
		if(true)
		{
			if(C.p>0)
			{
				if(word(code.toCharArray()[C.p-1]))
					C.p--;
			}
			if(!word(code.toCharArray()[C.p]))
			{
				if(C.p>code.length()-20)
				{
					while(!new Rule().word(code.toCharArray()[C.p])&&C.p>0)
						C.p--;
				}
				else
				{
					while(!new Rule().word(code.toCharArray()[C.p])&&C.p<code.length()-1)
						C.p++;
				}
					
			}
			int i1=C.p;
			int i2=C.p+1;
			
			while (word(code.toCharArray()[i1])&&i1>=0)
			{
				i1--;
			}
			while (word(code.toCharArray()[i2])&&i2<code.length()-1)
			{
				i2++;
			}
			String x="";
			if (i2>code.length()-1)i2=code.length()-1;
			if (i1<-1)i1=-1;
			if(i2<i1+1)i1=i2-1;
			x=code.substring(i1+1,i2);
			if(x.contains("catch"))return rule_9(code, C, res, res2,line);
			if(x.contains("try"))return rule_8(code, C, res, res2,line);
			if(x.contains("return"))return rule_5(code, C, res, res2,line);
			if(x.contains("import")||x.contains("package"))return rule_7(code, C, res, res2,line);
//			System.out.println(x);
			ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
			int count =1;
			int p1=0;
			String y="";
			for (int i=0;i<list.size();i++)
			{
				if(search_String(code,list.get(i)).contains(x))
				{
//					System.out.println("RIGHT:"+x+"          "+search_String(code,list.get(i)));
					return 4;
				}
//				else
//					System.out.println("NOPE:"+search_String(code,list.get(i)));
			}
		}
		return -4;
	}
	public int rule_3(String code,Generation.CH C, String res, String res2, int line)
	{
		
		return 0;
	}
	public int rule_2(String code,Generation.CH C, String res, String res2, int line)///**/的匹配
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		for (int i=0;i<list.size();i++)
		{
			if(list.get(i)==line)
				return 2;
		}
		return 99;
	}
	public int rule_1(String code,Generation.CH C, String res, String res2, int line)//{}的匹配
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		for (int i=0;i<list.size();i++)
		{
			if(search_String(code,list.get(i)).contains("{")||search_String(code,list.get(i)).contains("}"))
				return 1;
			if(C.c2.contains("{")&&!C.c2.contains("}"))
			{
				
				for (int i1=0;i<list.size();i++)
				{
					if(search_String(code,list.get(i1)).contains("}"))
						return 1;
				}
				
			}
			if(C.c3.contains("{")&&!C.c3.contains("}")) 
			{
				for (int i2=0;i<list.size();i++)
				{
					if(list.get(i2)>=line||search_String(code,list.get(i2)).contains("{")) 
						return 1;
				}
				if(res.contains("insert \"}\" "))
					return 1;
			}
			if(C.c2.contains("}")&&!C.c2.contains("{"))
			{
				for (int i2=0;i<list.size();i++)
				{
					if(list.get(i2)>=line||search_String(code,list.get(i2)).contains("{"))
						return 1;
				}
				
			}
			if(C.c3.contains("}")&&!C.c3.contains("{")) 
			{
				for (int i2=0;i<list.size();i++)
				{
					if(list.get(i2)<=line||search_String(code,list.get(i2)).contains("}")) 
						return 1;
				}
			}
			
		}	
		return 100;
	}
	public int rule_1_1(String code,Generation.CH C, String res, String res2, int line)//()的匹配
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		for (int i=0;i<list.size();i++)
		{
			
			if(search_String(code,list.get(i)).contains("(")||search_String(code,list.get(i)).contains(")"))
				return 1;
			if(C.c2.contains("(")&&!C.c2.contains(")"))
			{
				
				for (int i1=0;i<list.size();i++)
				{
					if(search_String(code,list.get(i1)).contains(")"))
						return 1;
				}
			}
			if(C.c3.contains("(")&&!C.c3.contains(")")) 
			{
				for (int i2=0;i<list.size();i++)
				{
					if(list.get(i2)>=line||search_String(code,list.get(i2)).contains("(")) 
						return 1;
				}
			}
			if(C.c2.contains(")")&&!C.c2.contains("("))
			{
				for (int i2=0;i<list.size();i++)
				{
					if(list.get(i2)>=line||search_String(code,list.get(i2)).contains("("))
						return 1;
				}
				
			}
			if(C.c3.contains(")")&&!C.c3.contains("(")) 
			{
				for (int i2=0;i<list.size();i++)
				{
					if(list.get(i2)<=line||search_String(code,list.get(i2)).contains(")")) 
						return 1;
				}
			}
			
		}	
		return 100;
	}
	public int rule_0(String res, String res2, int line)
		{
			ArrayList<Integer> list1=search_line1(res);//放入所有的报错行数
			ArrayList<Integer> list2=search_line2(res2);
			if(list1.contains(line)&&list2.contains(line))
				return -1;
			return 0;
		}
	public boolean rule_0_0(String res, String res2, int line)
	{
		ArrayList<Integer> list1=search_line1(res);//放入所有的报错行数
		ArrayList<Integer> list2=search_line2(res2);
		if(list1.contains(line)||list2.contains(line))
			return false;
		return true;
	}
	public boolean contains_token(String code)
	{
		for(int i=0;i<code.length();i++)
		{
			if(!word(code.toCharArray()[i])&&code.toCharArray()[i]!='\n'&&code.toCharArray()[i]!=' ')
				return true;
		}
		return false;
	}
	public int tp_11(String code, Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line1(res);
		String[] items=res.split("----------");
		for(String item:items) if(item.contains(". expected")&&item.contains("at line "+(line+1))){
			return 111;
		}
		return 0;
	}
	public int tp_10(String code, Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		if(!C.c2.contains("}")&&!C.c3.contains("{")) 
		{
			if(res.toString().contains("insert \"}\" to complete ClassBody"))
				return 110;
		}
		return 0;
	}
	public int tp_9(String code, Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		if(list.contains(line-1)&&res.toString().contains("\") ;\" to complete BlockStatements"))
				return 109;
		return 0;
	}
	public int tp_8(String code, Generation.CH C, String res, String res2, int line)
	{
		ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
		if(C.c3.contains("%")&&list.size()==1)
		{
			if(res.toString().contains("insert \"}\""))
				return 108;
		}
			
		return 0;
	}
	public int tp_7(String code, Generation.CH C, String res, String res2, int line)//被注释符分隔
	{
		if(line>1)line--;
		int k=0;
		if(search_String(code,line).contains("*/"))
		{
			
			k=p2line(code,code.lastIndexOf("/*", C.p));
			ArrayList<Integer> list=search_line(res,res2);//放入所有的报错行数
			for (int i=0;i<list.size();i++)
			{
				if(list.get(i)==k)
				{
					return 107;
					
				}
			}
		}
		return 0;
	}
	public int tp_1(String code, Generation.CH C, String res, String res2, int line)
	{
		if(contains_token(C.c3))
		{
			String[] items=res.split("----------");
			for(String item:items) if(item.contains("ERROR in")){
				int  p=item.indexOf("(at line ");
				p+=9;
				int r=0;
				while(item.charAt(p)!=')') {
					r=r*10+item.charAt(p)-'0';
					p++;
				}
				if (item.contains(search_String(C.code,line)))
				{
					return 101;
				}
			}
		}
		return 0;
	}
	public int tp_6(String code, Generation.CH C, String res, String res2, int line)//关于()
	{
		if(C.c3.contains("String")||C.c3.contains("int")||C.c3.contains("float")||C.c3.contains("boolean")||C.c3.contains("class")||C.c3.contains("char"))
		{
			String[] items=res.split("----------");
			for(String item:items) if(item.contains("ERROR in")){
				int  p=item.indexOf("(at line ");
				p+=9;
				int r=0;
				while(item.charAt(p)!=')') {
					r=r*10+item.charAt(p)-'0';
					p++;
				}
				if (item.contains("insert \";\"")&&r>line)
				{
					return 106;
				}
			}
		}
		return 0;
	}
	public int tp_5(String code, Generation.CH C, String res, String res2, int line)//关于()
	{
		if(C.c3.contains("native"))
		{
			boolean flag=true;
			String[] items=res.split("----------");
			for(String item:items) if(item.contains("ERROR in")){
				int  p=item.indexOf("(at line ");
				p+=9;
				int r=0;
				while(item.charAt(p)!=')') {
					r=r*10+item.charAt(p)-'0';
					p++;
				}
				if (item.contains("native")||r==line+1)
				{
					flag=false;
					break;
				}
			}
			if(flag) {System.out.println("native："+res);return 105;}
		}
		return 0;
	}
	public int tp_4(String code, Generation.CH C, String res, String res2, int line)//关于()
	{
		if(C.c2.contains("(")||C.c2.contains(")"))
		{
			String[] items=res.split("----------");
			for(String item:items) if(item.contains("ERROR in")){
				int  p=item.indexOf("(at line ");
				p+=9;
				int r=0;
				while(item.charAt(p)!=')') {
					r=r*10+item.charAt(p)-'0';
					p++;
				}
				if (item.contains("token \"}\""))
				{
//					System.out.println("()错误");
					return 104;
				}
			}
		}
		return 0;
	}
	public int tp_3(String code, Generation.CH C, String res, String res2, int line)//关于return
	{
		boolean flag=true;
		if(search_String(code,p2line(code,C.p)).contains("return"))
		{
			String[] items=res.split("----------");
			for(String item:items) if(item.contains("ERROR in")){
				int  p=item.indexOf("(at line ");
				p+=9;
				int r=0;
				while(item.charAt(p)!=')') {
					r=r*10+item.charAt(p)-'0';
					p++;
				}
				if (item.contains("return"))
				{
					flag=false;
					break;
				}
			}
			if(flag) {System.out.println("缺少return："+res);return 103;}
		}
		return 0;
	}
	public int tp_2(String code, Generation.CH C, String res, String res2, int line)
	{
		
		ArrayList<Integer> list=search_line(res,res2);
		if(C.c2.contains("try")&&!C.c2.contains("{"))
		{
			boolean flag=false;
			int i=0;
			for(int k=C.p;k<code.length();k++)
			{
				if(i<=0&&flag)
				{
					for (int ii=0;ii<list.size();ii++)
					{
						if(list.get(ii)==p2line(code,k))
						{
							System.out.println("关于try");
							return 102;
						}
					}
					break;
				}
				if(code.toCharArray()[k]=='{') 
					i++;
				if(code.toCharArray()[k]=='}')
				{
					i--;
					flag=true;
				}
			}
		}
		return 0;
	}
}
