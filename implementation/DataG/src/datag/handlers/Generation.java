package datag.handlers;
import datag.handlers.Rule;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.internal.compiler.Compiler;
import org.eclipse.jdt.internal.compiler.DefaultErrorHandlingPolicies;
import org.eclipse.jdt.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.ICompilerRequestor;
import org.eclipse.jdt.internal.compiler.IErrorHandlingPolicy;
import org.eclipse.jdt.internal.compiler.IProblemFactory;
import org.eclipse.jdt.internal.ui.text.correction.AssistContext;
import org.eclipse.jdt.internal.ui.text.correction.ProblemLocation;
import org.eclipse.jdt.internal.ui.text.correction.QuickFixProcessor;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI; 
import java.sql.*;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import javassist.compiler.Javac;
public class Generation {
	IProject project;
	IJavaProject javaProject;
	String[] key= {"abstract","assert","boolean","break","byte",
			"case","catch","char","class","const","continue","default",
			"do","double","else","enum","extends","final","finally","float",
			"for","goto","if","implements","import","instanceof","int","interface",
			"long","native","new","package","private","protected","public","return",
			"short","static","strictfp","super","switch","synchronized","this","throw",
			"throws","transient","try","void","volatile","while"};
	String[] token= {",",".","(",")","[","]","{","}",";","@","&&","||",
			"!","%","+","-","*","/","&","|","~","#"};
	String [] bio_token= {"{","}","[","]","(",")","\'","\"","/*","*/"};
	String []words=null;
	String word="";
	ArrayList<Integer> num1=new ArrayList();
	ArrayList<Integer> num2=new ArrayList();
	float tot=0;
	float unpass=0;
	float tp=0;//真案例
	float fp=0;//假案例
	float JE_1=0;
	float JE_2=0;
	ArrayList<Double> weight=new ArrayList();
	public void count(int x)
	{
		if(x<50)fp++;
		else tp++;
		if(num1.size()<1)
		{
			num1.add(x);
			num2.add(1);
		}
		else
		{
			if(num1.contains(x))
			{
				num2.set(num1.indexOf(x), num2.get(num1.indexOf(x))+1);
			}
			else
			{
				num1.add(x);
				num2.add(1);
			}
		}
			
	}
	
	public boolean print_num()
	{
		if(num1.size()<1)
			return false;
		int i,j,p;
		int x,y;
		x=0;
		y=200;
		p=0;
		for(i=0;i<num1.size();i++)
		{
			y=200;
			for(j=0;j<num1.size();j++)
			{
				if(num1.get(j)<y&&num1.get(j)>x)
				{
					y=num1.get(j);
					p=num1.indexOf(y);
				}
			}
			System.out.print("["+num1.get(p)+":"+num2.get(p)+"]    ");
			x=y;
		}
		System.out.println();
		System.out.println("未通过率："+unpass+"/"+tot+"="+unpass/tot);
		System.out.println("T/F："+tp+"/"+fp+"="+tp/fp);
		return true;
	}
	public Generation(IProject project,IJavaProject javaProject) {
		this.project=project;
		this.javaProject=javaProject;
	}
	

	public String cleancode(String x)
	{
		int a=x.indexOf("/*");
		int b=x.indexOf("*/");
		while(a>0&&b>a)
		{
			x=x.substring(0, a-1)+x.substring(b+1, x.length()-1);
			a=x.indexOf("/*");
			b=x.indexOf("*/");
		}
		System.out.println("step1");
		int c=x.indexOf("//");	
		while(c>0)
		{
			int i=0;
			for(i=c;i<x.length();i++)
			{
				if(x.charAt(i)=='\n')
					break;
			}
			x=x.substring(0, c-1)+x.substring(i+1,x.length()-1);
			c=x.indexOf("//");
		}
		return x;
	}

	public String decode(String x)
	{
		System.out.println("ready!");
		x=cleancode(x);
		System.out.println("clean!!!");
		String y="";
		int i,j1,j2;
		i=j1=j2=0;
		for (i=0;i<x.length();i++)
		{
			if (x.charAt(i)=='{')
			{
				j1=j2=i;
				for(;j2<x.length();j2++)
				{
					if (x.charAt(j2)=='}')
						break;
				}
				if(j2-1>j1+1)
					y+=decode(x.substring(j1+1, j2-1))+"\n*********\n";
				i=j2;
			}
		}
		System.out.println();
		if(j2<x.length()-1)
			y+=x.substring(0,j1)+x.substring(j2,x.length()-1)+"\n*********\n";
		return y;
	}

	public int position_change(String x,String y)
	{
		int k=0;
		int kk=1;
		while (k<x.length()&&k<y.length())
		{
			if(x.charAt(k)!=y.charAt(k))
				return kk;
			else
				if(x.charAt(k)=='\n')
					kk++;
			k++;
		}
		return -1;
	}
	class CH
	{
		String code;
		int c1;//变异
		String c2;//被操作
		String c3;//操作
		int c4;//操作符
		int p;//位置
		String[] name;
	};
	public void gener()
	{
		while(true)
		{
		float mu_total=0;
		float ec_true=0;
		float ec_false=0;
		float ec_tp=0;
		float ec_fp=0;
		System.out.println("AAAAAAAAAAAAAAAAAAA");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");	
			Connection conn = DriverManager.getConnection(
	                "jdbc:mysql://localhost:3306/wmy?serverTimezone=GMT&useSSL=false&useUnicode=true&amp",
	                "root","123456");
			String sql="insert into data values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String sql2="insert into examples values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			if(conn.isClosed()) {System.out.println("未连接数据库");}
			else System.out.println(conn);
			PreparedStatement ps=conn.prepareStatement(sql);
			AST ast=AST.newAST(AST.JLS9);
			int Total=0;
			String[] name= {};
			String name1="";
			for(IPackageFragment pf:javaProject.getPackageFragments()) {
				for(ICompilationUnit icu:pf.getCompilationUnits()) {
					CompilationUnit cu=createCompilationUnit(icu);
					
					name1+=","+cu.getJavaElement().getElementName().substring(0,cu.getJavaElement().getElementName().length()-6 );
//					System.out.println(name1);	
				}
			}
			name=name1.substring(1,name1.length()).split(",");
			long start=System.currentTimeMillis(),end;
			int count=0;
			for(IPackageFragment pf:javaProject.getPackageFragments()) {
				for(ICompilationUnit icu:pf.getCompilationUnits()) {
					start =System.currentTimeMillis();
					CompilationUnit cu=createCompilationUnit(icu);
					System.out.println(cu.getJavaElement().getElementName()+"  ------  Begin");			
					String path="C:\\DataG\\"+icu.getElementName();	
					File code=new File(path);
					code.createNewFile();
					String libs=getClasspath("C:\\projects"+project.getFullPath().toOSString()+"\\lib");
					path+=" -classpath \""+libs+"C:\\projects"+project.getFullPath().toString()+"/src/\"";
					String res=compileCode(cu.toString(),path);
					
					if(calLineNum(res)!=-1) {System.out.println(icu.getElementName()+"有错误");continue;}
					
					Random r=new Random();
					String rw=return_words(cu.toString());
					word+=rw;
					words=word.substring(0,word.length()).split(",");//词表
					int T=200;//变异数量
					
					while(T-->0) {
						tot++;
//						System.out.print('.');
						if(T%100==0)
						{
							System.out.println("----");
							System.out.println("总数："+mu_total+"  通过："+ec_true+"  未通过："+ec_false+"  正例："+ec_tp+"  负例："+ec_fp);
							System.out.println("总数："+mu_total+"  每项都不一致："+(mu_total-JE_1)+" "+((mu_total-JE_1)/mu_total)+"  至少一项不一致："+(mu_total-JE_2)+" "+((mu_total-JE_2)/mu_total));
//							System.out.println("比例："+ec_true+"\\"+mu_total+"="+ec_true/mu_total);
							print_num();
						}
						if(T%100==0)
						{
							end=System.currentTimeMillis();
							System.out.println("耗时："+(end-start)/1000);
							start=System.currentTimeMillis();
						}
						String source="";
						String mod="";
						int flag=r.nextInt(7);
						int flag_p=r.nextInt(9);
//						int flag=5;//对应修改
						CH C=new CH();
						
						C.c1=0;
						C.c2="";
						C.c3="";
						
						C.p=0;
						C.name=name;

						int r1=posi(cu.toString(),flag_p);
						int r2;
						int r3=0;
						flag=r.nextInt(2);
						C=gener(cu.toString(),flag);
//						flag=r.nextInt(3);
						
						switch (flag)
						{
						case 0:
							r2=r.nextInt(3);
							switch(r2)
							{
							case 0:
								r3=r.nextInt(key.length);
								C.c3=key[r3];
								break;
							case 1:
								r3=r.nextInt(token.length);
								C.c3=token[r3];
								break;
							case 2:
								r3=r.nextInt(words.length);
								C.c3=words[r3];
								break;
							}
							source=code_insert(cu.toString(),r1,r2,r3);
							C.c2="";
							break;
						case 1:
							int rr=r.nextInt(3);
							int r5,r4=0;
							r5=r.nextInt(10);
							if (cu.toString().toCharArray()[r1]=='\n')
							{
								r1=r.nextInt(cu.toString().length());
							}
							if (r1+r5>=cu.toString().length())
								r5=cu.toString().length()-r1-2;
							if (r1<1)
								r1=1;
							if(cu.toString().substring(r1-1, r1+r5+1).contains("\n"))
								r5=cu.toString().indexOf("\n", r1)-r1-1;
							C.c2=cu.toString().substring(r1-1, r1+r5+1);
							rr=6;
							switch(rr)
							{
							case 0:
								r4=r.nextInt(key.length);
								C.c3=key[r4];
								break;
							case 1:
								r4=r.nextInt(token.length);
								C.c3=token[r4];
								break;
							case 2:
								r4=r.nextInt(words.length);
								C.c3=words[r4];
								break;
							}
							source=code_insert(code_delete(cu.toString(),r1,C.c2.length()),r1,rr,r4);
//							source=cu.toString().substring(0, r1)+cu.toString().substring(r1+r.nextInt(cu.toString().indexOf("\n", r1)+1-r1));
							break;
						case 2:
							int r22= r.nextInt(10);
							
							if(r1+r22+1>=cu.toString().length())
								r22= cu.toString().length()-1-r1;
							if (r1<1)
								r1=1;
							if(cu.toString().substring(r1-1, r1+r22+1).contains("\n"))
								r22=cu.toString().indexOf("\n", r1)-r1-1;
							C.c2=cu.toString().substring(r1-1, r1+r22+1);
							source=code_delete(cu.toString(),r1,r22);
							break;
						case 3:
//							System.out.println("注意！");
							C.c2="";
							C.c3=mix(cu.toString());
							source=cu.toString().substring(0,r1)+C.c3+cu.toString().substring(r1,cu.toString().length());
//							System.out.println(C.c3);
							break;
						case 4:
							if(r1>cu.toString().length()-10)
								r1=cu.toString().length()-10;
							C.c2=code_change1_1(cu.toString(),r1);
							C.c3="";
							source=code_change1(cu.toString(),r1);
							break;
//						case 5:
//							C.c2=code_change2_1(cu.toString(),r1);
//							C.c3="";
////							source=code_change2(cu.toString(),r1);
//							source=cu.toString();
//							break;
						case 6:
							C.c2=code_change3_1(cu.toString(),r1);
							C.c3="";
							source=code_change3(cu.toString(),r1);
							break;
//						case 7:
//							if(r1+1>=cu.toString().length())C.c2=cu.toString().substring(r1-1);
//							else C.c2=new Rule().search_word(cu.toString(), r1);
//							C.c3=code_line(cu.toString());
//							source=cu.toString();
////							source=cu.toString().substring(0,r1)+C.c3+cu.toString().substring(r1,cu.toString().length());
//							break;
						case 5:
							int r8=r.nextInt(cu.toString().length());
							C.c2=new Rule().search_word(cu.toString(), r1);
							C.c3=new Rule().search_word(cu.toString(), r8);
							source=code_change4(cu.toString(),r1,r8);
							break;
						}
						C.c1=flag;
						C.p=r1;
						C.c4=flag_p;
						C.code=source;
						FileWriter fw=new FileWriter(code);
						fw.write(source);
						fw.close();
//						System.out.println("代码处理完成");
						//path+=" -classpath \"C:\\Programs\\eclipse oxygen\\runtime-EclipseApplication"+project.getFullPath().toString()+"/src/\"";
						//C:\DataG\projects
						libs=getClasspath("C:\\projects"+project.getFullPath().toOSString()+"\\lib");
						path="C:\\DataG\\"+icu.getElementName();
						path+=" -classpath \""+libs+"C:\\projects"+project.getFullPath().toString()+"/src/\"";
						//System.out.println(path);
						res=compileCode(cu.toString(),path);
						String res2=javaccode("",path);
						mu_total++;
						if(res.length()==0)
							ec_true++;
						else
							ec_false++;
						if(res.length()!=0) {
							
							int ln=calLineNum(res);
							if(ln==-1) continue;
							int position=1;
							boolean yes=false;
//							System.out.println(res2);
							if(new Rule().compare_line2(new Rule().search_line1(res),new Rule().search_line2(res2)))//至少一项一致
							{
//								System.out.println("注意！！！");
								
								JE_2++;
							}
							if(new Rule().compare_line(new Rule().search_line1(res),new Rule().search_line2(res2)))//至少一项一致
							{
//								System.out.println(new Rule().search_line1(res));
//								System.out.println(new Rule().search_line2(res2));
								JE_1++;
							}
							for(int i=0;i<Math.min(cu.toString().length(), source.length());i++) {
								if(cu.toString().charAt(i)!=source.charAt(i)) {
									yes=true;
									break;
								}
								if(source.charAt(i)=='\n') position++;
							}
//							if(!yes) continue;
							int flag1=new Rule().situation(cu.toString(),C,res,res2,position);
/*							if(flag1>50)
							{
								count++;
								if(count%100==99)
								{
									end=System.currentTimeMillis();
									System.out.println("耗时："+(start-end)/1000);
									start=System.currentTimeMillis();
								}
							}
*/
							if(flag1<=0) continue;
							unpass++;
							count(flag1);
							if(flag1<50)
								ec_fp++;
							else
								ec_tp++;
//							if(C.c2.contains("\n")||C.c3.contains("\n")||num2.get(num1.indexOf(flag1))>5) continue;
//							if(C.c2.contains("\n")||C.c3.contains("\n")) continue;
//							if(flag1>0)

//							boolean in=false;
//							if(new Rule().compare_line(new Rule().search_line1(res),new Rule().search_line2(res2)))//至少一项一致
//							{
////								if(new Rule().rule_0_0(res, res2, position)&&(flag1>50||flag1==0))//不包含修改行，且eclipse错误
////								{
////									in=true;
////									ps=conn.prepareStatement(sql2);
////									System.out.println("写入");
////								}
////								else
////									continue;
//								JE_1++;
//							}
//							
//							else//任何项都不重合
//							{
//								mu_total++;
//								if(flag1<50)
//									ec_true++;
//								in=true;
//								ps=conn.prepareStatement(sql);
//							}
////							if(new Rule().compare_line2(new Rule().search_line1(res),new Rule().search_line2(res2)))//至少一项一致
////							{
////								JE_2++;
////							}
//							if(ln!=position_change(cu.toString(),source)) 
//							if(in)
//							{
//								
//								ps.setString(1, cu.getJavaElement().getJavaProject().getElementName());//project
//								ps.setString(2, cu.getPackage().getName().toString());//package
//								ps.setString(3, cu.getJavaElement().getElementName());//class
//								ps.setString(4, cu.toString());//ori
//								ps.setString(5, source);//mod
//								ps.setInt(6, position);//position
//								ps.setString(7, new Rule().search_line1(res).toString());//报告位置
//								ps.setString(8,res);
//								ps.setString(9, res2);
//								ps.setString(10, new Rule().search_line2(res2).toString());
//								ps.setInt(11,C.c1);//操作
//								ps.setString(12,C.c2);//被操作
//								ps.setString(13,C.c3);//操作体
//								ps.setInt(14, flag1);
//								ps.execute();
//								Total++;
//							}		
					}

					}		
//					code.delete();
					
				}
			}
			System.out.println(Total);
			ps.close();			
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}
}	
	protected CompilationUnit createCompilationUnit(ICompilationUnit compilationUnit) {
		ASTParser parser = ASTParser.newParser(AST.JLS9);
		parser.setSource(compilationUnit);
		parser.setProject(compilationUnit.getJavaProject());
		IPath path=compilationUnit.getPath();
		parser.setUnitName(path.toString());
		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		CompilationUnit unit=null;
		try
		{
			unit= (CompilationUnit) parser.createAST(null);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return unit;
	}
	private String javaccode(String code, String path) {
//		OutputStream out = new ByteArrayOutputStream();
//		PrintWriter pw= new PrintWriter(out,true);
//		
//		OutputStream eout = new ByteArrayOutputStream();
//		PrintWriter epw= new PrintWriter(eout,true);
//		.compile(path, pw, epw, null);
		//System.out.println("===================="+path);
		try {
			Process process = Runtime.getRuntime().exec("javac "+path);
            InputStream inputStream = process.getErrorStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = null,res="";
            while ((line = bufferedReader.readLine()) != null) {
                res+=line+"\r\n";
            }
            
            return res;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "";
//		return eout.toString();
	}
	private String compileCode(String code,String path) {
//		System.out.println("===================="+path);
		OutputStream out = new ByteArrayOutputStream();
		PrintWriter pw= new PrintWriter(out,true);
		
		OutputStream eout = new ByteArrayOutputStream();
		PrintWriter epw= new PrintWriter(eout,true);
		BatchCompiler.compile(path, pw, epw, null);
		return eout.toString();
	}
	
	private String code_insert(String code,int r1,int r2,int r3) {

		switch(r2)
		{
		case 0:
			return code.substring(0, r1)+key[r3]+code.substring(r1, code.length());
		case 1:
			return code.substring(0, r1)+token[r3]+code.substring(r1, code.length());
		case 2:
			return code.substring(0, r1)+words[r3]+code.substring(r1, code.length());
		}
		return code;
	}
	
	public String code_line(String code)//随机一行
	{
		Random r=new Random();
		int j;
		String x;
		j=r.nextInt(code.length());
//		System.out.println("位置："+j);
		j=new Rule().p2line(code, j);
//		System.out.println("行"+j);
		x=new Rule().search_String(code, j);
		return x;
	}
	public String mix(String code)
	{
		String x="";
		Random r=new Random();
		int i=r.nextInt(5);
		int flag=0;
		int j;
		for (;i>=0;i--)
		{
			flag=r.nextInt(4);
//			System.out.println("flag:"+flag);
			switch (flag)
			{
			case 0:
				j=r.nextInt(key.length);
				x+=key[j];
				break;
			case 1:
				j=r.nextInt(token.length);
				x+=token[j];
				break;

			case 2:
				j=r.nextInt(code.length()-10);
				x+=code.substring(j,j+r.nextInt(10)).replace('\n', ' ');
				break;
			case 3:
				j=r.nextInt(words.length);
				x+=words[j];
				break;
			}
			
		}
		return x;
	}
	public String code_change4(String code, int r1,int r2)//替换
	{
		String x= new Rule().search_word(code,r1);
		int r;
		if(r1<x.length())r=0;
		else r=r1-x.length();
		int p=code.indexOf(x, r);
		if(p<0)
			p=code.indexOf(x);
		String x2=code.substring(0,p)+new Rule().search_word(code,r2)+code.substring(p+x.length());
		return x2;
	}
	public String code_change3_1(String code,int r1)
	{
		if(!new Rule().word(code.toCharArray()[r1]))
		{
			if(r1>code.length()-20)
			{
				while(!new Rule().word(code.toCharArray()[r1])&&r1>0)
					r1--;
			}
			else
			{
				while(!new Rule().word(code.toCharArray()[r1])&&r1<code.length()-1)
					r1++;
			}
				
		}
		int i1=r1;
		int i2=r1+1;
		if (i2>=code.length())i2=code.length()-1;
		while(i1>=0&&new Rule().word(code.toCharArray()[i1]))
			i1--;
		while(new Rule().word(code.toCharArray()[i2])&&i2<code.length()-1)
			i2++;
		if (i2>code.length()-1)i2=code.length()-1;
		if (i1<-1)i1=-1;
		if(i2<i1+1)i1=i2-1;
		return code.substring(i1+1,i2);
	}

	public String code_change3(String code,int r1)//删除一个词
	{
		
		if(!new Rule().word(code.toCharArray()[r1]))
		{
			if(r1>code.length()-20)
			{
				while(!new Rule().word(code.toCharArray()[r1])&&r1>0)
					r1--;
			}
			else
			{
				while(!new Rule().word(code.toCharArray()[r1])&&r1<code.length()-1)
					r1++;
			}
				
		}
		int i1=r1;
		int i2=r1+1;
		if (i2>=code.length())i2=code.length()-1;
		while(i1>0&&new Rule().word(code.toCharArray()[i1]))
			i1--;
		while(new Rule().word(code.toCharArray()[i2])&&i2<code.length()-1)
			i2++;
		return code.substring(0, i1)+code.substring(i2);
		
	}

	public String code_change1_1(String code,int r1)//删除空格
	{
		int i1=code.indexOf(" ", r1);
		int i2=code.indexOf(" ", i1+1);
		if(i2<=i1||i1<0)
		{
			i2=code.lastIndexOf(" ");
			i1=code.lastIndexOf(code, i2-1);
		}
		
		String x=code.substring(i1,i2);
		return x;
	}
	public String code_change1(String code,int r1)
	{
		int i1=code.indexOf(" ", r1);
		int i2=code.indexOf(" ", i1+1);
		if(i2<=i1||i1<0)
		{
			i2=code.lastIndexOf(" ");
			i1=code.lastIndexOf(code, i2-1);
		}
		String x=code.substring(0,i1)+code.substring(i2);
		return x;
	}
	public String code_change2_1(String code,int r1)
	{
		String x="";
		x=new Rule().search_String(code,new Rule().p2line(code, r1));
		return x;
	}
	public String code_change2(String code,int r1)//删除一行
	{
		String x="";
		x=new Rule().search_String(code,new Rule().p2line(code, r1));
		String x2="";
		x2=code.substring(0,code.indexOf(x))+code.substring(code.indexOf(x)+x.length());
		return x2;
	}
	public boolean contain_key(String code)
	{
		for(int i=0;i<key.length;i++)
		{
			if(code.contains(key[i]))
			{
				String x=code.replaceAll(key[i], "");
				for(int j=0;j<x.length();j++)
				{
					if(new Rule().word(x.toCharArray()[j]))
						return false;
				}
				return true;
			}
		}
		return false;
	}
	public int posi(String code,int situation)
	{
		Random r=new Random();
		int x= r.nextInt(code.length());
		int flag;
		String x2;
		switch(situation) {
		case 0://关键词定位
			flag=10000;
			
			while(flag>0)
			{
				x2=new Rule().search_word(code, x);
				if(contain_key(x2))
					return x;
				else x= r.nextInt(code.length());
				flag--;
			}
			break;
		case 1://非关键词定位
			flag=10000;
			while(flag>0)
			{
				if(!contain_key(new Rule().search_word(code, x)))
					return x;
				else x= r.nextInt(code.length());
				flag--;
			}
			break;
		case 2://临近符号
			flag=10000;
			while(flag>0)
			{
				x2=new Rule().search_word(code, x);
				if(contain_key(x2))
					break;
				else x= r.nextInt(code.length());
				flag--;
			}
			boolean flag2=r.nextBoolean();
			while(new Rule().word(code.toCharArray()[x])&&x>0&&x<code.length()-1)
			{
				if(flag2)
					x++;
				else x--;
			}
			
			return x;
		case 3:
			return r.nextInt(code.length());

		case 4:
			return num(code);
		case 5://孤独符号位
			return seek_token(code);
		case 6://多元符号位
			return seek_multitoken(code);
		case 7://成对匹配符号
			return seek_biotoken(code);
		case 8://行的首尾
			return seek_l(code);
		}
		return 0;
		
	}
	public int seek_l(String code)
	{
		int flag =100;
		Random r=new Random();
		int i=0;
		int j;
		while(flag>0)
		{
			j=r.nextInt(code.length());
			
			i=code.indexOf("\n", j);
			if(i>0)
				return i;
		}
		return (code.indexOf("\n"));
		
		
	}
	public int seek_biotoken(String code)
	{
		int flag=1000;
		int i=0;
		int i1,i2;
		while(flag>0)
		{
			i=seek_token(code);
			if (i<=0)i1=0;
			else i1=i-1;
			if(i>=code.length()-1)i2=code.length();
			else i2=i+1;
			for(int j=0;j<bio_token.length;j++)
			{
				if(code.substring(i1,i2).contains(bio_token[j]))
					return i;
			}
			flag--;
		}
		return -1;
	}
	
	public int seek_multitoken(String code)
	{
		Random r=new Random();
		int p=r.nextInt(code.length());
		boolean f=r.nextBoolean();
		while(p>=0&&p<code.length()-1)
		{
			if(!new Rule().word(code.toCharArray()[p])&&!new Rule().word(code.toCharArray()[p+1]))
				return p;
			if(f)p++;
			else p--;
		}
		p=0;
		while(p<code.length())
		{
			if(!new Rule().word(code.toCharArray()[p])&&!new Rule().word(code.toCharArray()[p+1]))
				return p;
			p++;
		}
		return seek_token(code);
	}
	public int seek_token(String code)
	{
		Random r=new Random();
		int p=r.nextInt(code.length());
		boolean f=r.nextBoolean();
		while(p>=0&&p<code.length())
		{
			if(!new Rule().word(code.toCharArray()[p]))
				return p;
			if(f)p++;
			else p--;
		}
		p=0;
		while(p<code.length())
		{
			if(!new Rule().word(code.toCharArray()[p]))
				return p;
			p++;
		}
		return -1;
	}
	public String return_words(String code)
	{
		String x="";
		int i,i1,i2;
		for (i=0;i<code.length();i++)
		{
			i1=i;
			while(new Rule().word(code.toCharArray()[i])&&i<code.length())
			{
				i++;
			}
			i2=i;
			if(!word.contains(code.substring(i1,i2))&&!x.contains(code.substring(i1,i2)))
				x+=','+code.substring(i1,i2);
		}
		return x;
	}
	public int num(String code)
	{
		Random r=new Random();
		int p=r.nextInt(code.length());
		boolean f=r.nextBoolean();
		while(p>=0&&p<code.length())
		{
			if(code.toCharArray()[p]>='0'||code.toCharArray()[p]<='9')
				return p;
			if(f)p++;
			else p--;
		}
		p=0;
		while(p<code.length())
		{
			if(code.toCharArray()[p]>='0'||code.toCharArray()[p]<='9')
				return p;
			p++;
		}
		return -1;
	}
	private String code_delete(String code,int r1,int r2) {
		if(r1+r2>=code.length())
			return code.substring(0,r1);
		return code.substring(0, r1)+code.substring(r1+r2);
	}
	
	private String replace(String a,String b,String c) {
		int pos=a.indexOf(b);
		return a.substring(0, pos)+c+a.substring(pos+b.length());
	}
	
	private void insertDB(CompilationUnit cu,String mod_code,Connection conn) {
		String ori_code=cu.toString();
		int ln=1,pos=0;
		while(ori_code.charAt(pos)==mod_code.charAt(pos)) {
			if(ori_code.charAt(pos)=='\n')
				ln++;
			pos++;
		}
		try {
			PreparedStatement ps=conn.prepareStatement("insert into data values (?,?,?,?,?,?)");
			ps.setString(1, cu.getJavaElement().getJavaProject().getElementName());
			ps.setString(2, cu.getPackage().getName().toString());
			ps.setString(3, cu.getJavaElement().getElementName());
			ps.setString(4, cu.toString());
			ps.setString(5, mod_code);
			ps.setInt(6, ln);
			ps.execute();
		}catch(Exception e) {
			
		}
	}
	private int calLineNum(String info) {
		String[] items=info.split("----------");
		for(String item:items) if(item.contains("ERROR in")){

			int  p=item.indexOf("(at line ");
			p+=9;
			int res=0;
			while(item.charAt(p)!=')') {
				res=res*10+item.charAt(p)-'0';
				p++;
			}
			return res;
		}
		return -1;
	}
	
	private String getClasspath(String path) {
		String res="";
		File f=new File(path);
		String[] files=f.list();
		for(String file : files)
			res+=path+"\\"+file+";";
		return res;
	}
	public int random_search_keyword (String code)
	{
		Random r=new Random();
		int i,j=0,p=0;
		boolean f=true;
		do {
			i=r.nextInt(code.length());
			j=r.nextInt(key.length);
			if(code.substring(i).contains(key[j]))
			{
				p=code.indexOf(key[j],i);
				f=false;
				break;
			}
			}
		while(f);
		return p;
	}
	public boolean word(char x)
	{
		if(x<='z'&&x>='a'||x>='A'&&x<='Z')
			return true;
		else
			return false;
	}
	public int search_wordtail(String code, int p)
	{
		int t=p+1;
		for(;t<code.length();t++)
		{
			if(!word(code.toCharArray()[t]))
				break;
		}
		return t;
	}
	public int random_search_character (String code)
	{
		Random r=new Random();
		int i,j=0,p=0;
		boolean f=true;
		do {
			i=r.nextInt(code.length());
			j=r.nextInt(token.length);
			if(code.substring(i).contains(token[j]))
			{
				p=code.indexOf(token[j],i);
				f=false;
			}
			}
		while(f);
		return p;
	}
	public CH gener(String code,int flag)
	{
		String mutant="";
		Random r=new Random();
		int i;
		CH C=new CH();
		
		C.c1=flag;
		C.c4=flag;
		C.c2="";
		C.c3="";		
		C.p=0;
//		C.name=name;
		switch (flag)
		{
		case 0://remove a keyword
			C.p=random_search_keyword(code);
			mutant=code.substring(0, C.p)+code.substring(search_wordtail(code,C.p));
			C.c2=code.substring(C.p,search_wordtail(code,C.p));
//			System.out.println("|||||"+code.substring(C.p,search_wordtail(code,C.p))+"-------\n"+C.c2);
			break;
		case 1://remove a character
			C.p=random_search_character(code);
			mutant=code.substring(0, C.p)+code.substring(C.p+1);
			C.c2=code.substring(C.p,C.p+1);
//			System.out.println("******"+C.c2);
			break;
		case 2://remove a token including a character
			
		}
		return C;
	}
}




