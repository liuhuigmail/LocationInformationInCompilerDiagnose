package datag.handlers;


public class InputGeneration {

	private String code;
	private boolean[] flag;
	private int[] father;
	private int[] stack;
	
	public InputGeneration(String code) {
		this.code=code;
		flag=new boolean[code.length()];
		for(int i=0;i<code.length();i++) 
			flag[i]=false;
		father=new int[code.length()];
		stack=new int[code.length()];
	}
	
	private void buildTree() {
		int size=0;
		stack[size++]=-1;
		for(int i=0;i<code.length();i++) {
			if(code.charAt(i)=='{') {
				father[i]=stack[size-1];
				stack[size++]=i;
				
			}
			else if(code.charAt(i)=='}') {
				size--;
				father[i]=stack[size-1];
			}
			else {
				father[i]=stack[size-1];
			}
		}
		
	}
	
	private void keepCode(int line) {
		int cline=1;
		int pos=0;
		while(cline!=line) {
			if(code.charAt(pos)=='\n') cline++;
			pos++;
		}
		while(pos!=-1) {
			int f=father[pos];
			for(int i=0;i<code.length();i++) if(father[i]==f)
				flag[i]=true;
			pos=f;
		}
	}
	
	public String getInput(int[] list) {
		buildTree();
		for(int i=0;i<list.length;i++)
			keepCode(list[i]);
		String res="";
		
		
		for(int i=0;i<code.length();i++) if(flag[i]) {
			res+=code.charAt(i);
		}
		
		String[] statements=res.split("\r\n");
		res="";
		for(int i=0;i<statements.length;i++) if(statements[i].length()>0)
			res+=statements[i]+"\r\n";
		
		return res;
	}


}
