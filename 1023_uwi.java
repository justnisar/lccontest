class Solution {
	    public boolean queryString(String S, int N) {
	    	if(N > 500000)return false;
	        SuffixAutomatonOfBit sa = build(S.toCharArray());
	        for(int i = 1;i <= N;i++){
	        	if(sa.track(Integer.toBinaryString(i).toCharArray()) == null)return false;
	        }
	        return true;
	    }
	    
    	public SuffixAutomatonOfBit build(char[] str)
    	{
    		int n = str.length;
    		SuffixAutomatonOfBit sa = new SuffixAutomatonOfBit(n);
    		sa.len = str.length;
    		
    		SuffixAutomatonOfBit.Node last = sa.t0;
    		for(char c : str){
    			last = sa.extend(last, c);
    		}
    		
    		return sa;
    	}
    	
	    public class SuffixAutomatonOfBit {
	    	public Node t0;
	    	public int len;
	    	public Node[] nodes;
	    	public int gen;
	    	private boolean sortedTopologically = false;
	    	private boolean lexsorted = false;
	    	
	    	private SuffixAutomatonOfBit(int n)
	    	{
	    		gen = 0;
	    		nodes = new Node[2*n];
	    		this.t0 = makeNode(0, null);
	    	}
	    	
	    	private Node makeNode(int len, Node original)
	    	{
	    		Node node = new Node();
	    		node.id = gen;
	    		node.original = original;
	    		node.len = len;
	    		nodes[gen++] = node;
	    		return node;
	    	}
	    	
	    	public class Node
	    	{
	    		public int id;
	    		public int len;
	    		public char key;
	    		public Node link;
	    		private Node[] next = new Node[3];
	    		public Node original;
	    		public int np = 0;
	    		public long hit = 0;
	    		
	    		public int id(char c)
	    		{
//	    			return c >= 'a' ? c-'a' : c-'A'+26;
	    			return c-'a';
	    		}
	    		
	    		public void putNext(char c, Node to)
	    		{
	    			to.key = c;
	    			if(hit<<~id(c)<0){
	    				for(int i = 0;i < np;i++){
	    					if(next[i].key == c){
	    						next[i] = to;
	    						return;
	    					}
	    				}
	    			}
	    			hit |= 1L<<id(c);
	    			if(np == next.length){
	    				next = Arrays.copyOf(next, np*2);
	    			}
	    			next[np++] = to;
	    		}
	    		
	    		public boolean containsKeyNext(char c)
	    		{
	    			return hit<<~id(c)<0;
//	    			for(int i = 0;i < np;i++){
//	    				if(next[i].key == c)return true;
//	    			}
//	    			return false;
	    		}
	    		
	    		public Node getNext(char c)
	    		{
	    			if(hit<<~id(c)<0){
	    				for(int i = 0;i < np;i++){
	    					if(next[i].key == c)return next[i];
	    				}
	    			}
	    			return null;
	    		}
	    		
	    		public List<String> suffixes(char[] s)
	    		{
	    			List<String> list = new ArrayList<String>();
	    			if(id == 0)return list;
	    			int first = original != null ? original.len : len;
	    			for(int i = link.len + 1;i <= len;i++){
	    				list.add(new String(s, first - i, i));
	    			}
	    			return list;
	    		}
	    	}

	    	public Node extend(Node last, char c)
	    	{
	    		Node cur = makeNode(last.len+1, null);
	    		Node p;
	    		for(p = last; p != null && !p.containsKeyNext(c);p = p.link){
	    			p.putNext(c, cur);
	    		}
	    		if(p == null){
	    			cur.link = t0;
	    		}else{
	    			Node q = p.getNext(c); // not null
	    			if(p.len + 1 == q.len){
	    				cur.link = q;
	    			}else{
	    				Node clone = makeNode(p.len+1, q);
	    				clone.next = Arrays.copyOf(q.next, q.next.length);
	    				clone.hit = q.hit;
	    				clone.np = q.np;
	    				clone.link = q.link;
	    				for(;p != null && q.equals(p.getNext(c)); p = p.link){
	    					p.putNext(c, clone);
	    				}
	    				q.link = cur.link = clone;
	    			}
	    		}
	    		return cur;
	    	}
	    	
	    	public SuffixAutomatonOfBit lexSort()
	    	{
	    		for(int i = 0;i < gen;i++){
	    			Node node = nodes[i];
	    			Arrays.sort(node.next, 0, node.np, new Comparator<Node>() {
	    				public int compare(Node a, Node b) {
	    					return a.key - b.key;
	    				}
	    			});
	    		}
	    		lexsorted = true;
	    		return this;
	    	}
	    	
	    	public SuffixAutomatonOfBit sortTopologically()
	    	{
	    		int[] indeg = new int[gen];
	    		for(int i = 0;i < gen;i++){
	    			for(int j = 0;j < nodes[i].np;j++){
	    				indeg[nodes[i].next[j].id]++;
	    			}
	    		}
	    		Node[] sorted = new Node[gen];
	    		sorted[0] = t0;
	    		int p = 1;
	    		for(int i = 0;i < gen;i++){
	    			Node cur = sorted[i];
	    			for(int j = 0;j < cur.np;j++){
	    				if(--indeg[cur.next[j].id] == 0){
	    					sorted[p++] = cur.next[j];
	    				}
	    			}
	    		}
	    		
	    		for(int i = 0;i < gen;i++)sorted[i].id = i;
	    		nodes = sorted;
	    		sortedTopologically = true;
	    		return this;
	    	}
	    	
	    	// visualizer
	    	
	    	public String toString()
	    	{
	    		StringBuilder sb = new StringBuilder();
	    		for(Node n : nodes){
	    			if(n != null){
	    				sb.append(String.format("{id:%d, len:%d, link:%d, cloned:%b, ",
	    						n.id,
	    						n.len,
	    						n.link != null ? n.link.id : null,
	    						n.original.id));
	    				sb.append("next:{");
	    				for(int i = 0;i < n.np;i++){
	    					sb.append(n.next[i].key + ":" + n.next[i].id + ",");
	    				}
	    				sb.append("}");
	    				sb.append("}");
	    				sb.append("\n");
	    			}
	    		}
	    		return sb.toString();
	    	}
	    	
	    	public String toGraphviz(boolean next, boolean suffixLink)
	    	{
	    		StringBuilder sb = new StringBuilder("http://chart.apis.google.com/chart?cht=gv:dot&chl=");
	    		sb.append("digraph{");
	    		for(Node n : nodes){
	    			if(n != null){
	    				if(suffixLink && n.link != null){
	    					sb.append(n.id)
	    					.append("->")
	    					.append(n.link.id)
	    					.append("[style=dashed],");
	    				}
	    				
	    				if(next && n.next != null){
	    					for(int i = 0;i < n.np;i++){
	    						sb.append(n.id)
	    						.append("->")
	    						.append(n.next[i].id)
	    						.append("[label=")
	    						.append(n.next[i].key)
	    						.append("],");
	    					}
	    				}
	    			}
	    		}
	    		sb.append("}");
	    		return sb.toString();
	    	}
	    	
	    	public String label(Node n)
	    	{
	    		if(n.original != null){
	    			return n.id + "C";
	    		}else{
	    			return n.id + "";
	    		}
	    	}
	    	
	    	public String toDot(boolean next, boolean suffixLink)
	    	{
	    		StringBuilder sb = new StringBuilder("digraph{\n");
	    		sb.append("graph[rankdir=LR];\n");
	    		sb.append("node[shape=circle];\n");
	    		for(Node n : nodes){
	    			if(n != null){
	    				if(suffixLink && n.link != null){
	    					sb.append("\"" + label(n) + "\"")
	    					.append("->")
	    					.append("\"" + label(n.link) + "\"")
	    					.append("[style=dashed];\n");
	    				}
	    				
	    				if(next && n.next != null){
	    					for(int i = 0;i < n.np;i++){
	    						sb.append("\"" + label(n) + "\"")
	    						.append("->")
	    						.append("\"" + label(n.next[i]) + "\"")
	    						.append("[label=\"")
	    						.append(n.next[i].key)
	    						.append("\"];\n");
	    					}
	    				}
	    			}
	    		}
	    		sb.append("}\n");
	    		return sb.toString();
	    	}
	    	
	    	public Node[][] ilinks()
	    	{
	    		int n = gen;
	    		int[] ip = new int[n];
	    		for(int i = 1;i < n;i++)ip[nodes[i].link.id]++;
	    		Node[][] ilink = new Node[n][];
	    		for(int i = 0;i < n;i++)ilink[i] = new Node[ip[i]];
	    		for(int i = 1;i < n;i++)ilink[nodes[i].link.id][--ip[nodes[i].link.id]] = nodes[i];
	    		return ilink;
	    	}
	    	
	    	public Node track(char[] q)
	    	{
	    		Node cur = t0;
	    		for(char c : q){
	    			cur = cur.getNext(c);
	    			if(cur == null)return null;
	    		}
	    		return cur;
	    	}
	    }
	}	
