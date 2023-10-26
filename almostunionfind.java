import java.util.*;
import java.io.*;

public class almostunionfind {

    public static void main(String[] arg){
        Kattio io = new Kattio(System.in);
        while (io.hasMoreTokens()){    
            Integer n = io.getInt();
            Integer m = io.getInt(); 
            UFDS Set = new UFDS(n);
            for (Integer i = 0; i < m; i++){
                Integer op = io.getInt();
                if (op == 1){
                    Set.one(io.getInt(), io.getInt());
                }
                else if (op == 2){
                    Set.two(io.getInt(), io.getInt());
                }
                else if (op == 3){
                    Integer[] temp = Set.three(io.getInt());
                    io.println(temp[0] + " " + temp[1]);
                }
            }
        }
        io.close();
    }
}

class UFDS {
    public Integer[] set;
    public Integer[] rank;
    public Integer[] num;
    public Integer[] Sum; 
    public HashMap<Integer, HashSet<Integer>> child;
    public Integer size;

    public UFDS(Integer size){
        this.size = size;
        this.set = new Integer[size + 1];
        this.num = new Integer[size + 1];
        this.Sum = new Integer[size + 1];
        this.rank = new Integer[size + 1];
        this.child = new HashMap<Integer, HashSet<Integer>>();
        for (Integer i = 1; i < size+1; i++){
            set[i] = i;
            rank[i] = 1;
            num[i] = 1;
            Sum[i] = i;
        }
    }

    public Integer find(Integer val){
        if (set[val] == val){
            return val;
        }
        else{
            child.get(set[val]).remove(val);
            set[val] = find(set[val]);
            if (!child.containsKey(set[val])){
                child.put(set[val], new HashSet<Integer>());
                child.get(set[val]).add(val);
            }
            else{
                child.get(set[val]).add(val);
            }
            return set[val];
        }
    }

    public void one(Integer p, Integer q){
        Integer pr = find(p);
        Integer qr = find(q);
        if (pr != qr){
            if (rank[qr] > rank[pr]){ // join set contain p to set contain q
                num[qr] += num[pr];
                Sum[qr] += Sum[pr];
                set[pr] = qr;
                if (!child.containsKey(qr)){
                    child.put(qr, new HashSet<Integer>());
                    child.get(qr).add(pr);
                }
                else{
                    child.get(qr).add(pr);
                }
            }
            else{ // join set contain q to set contain p
                num[pr] += num[qr];
                Sum[pr] += Sum[qr];
                set[qr] = pr;
                if (!child.containsKey(pr)){
                    child.put(pr, new HashSet<Integer>());
                    child.get(pr).add(qr);
                }
                else{
                    child.get(pr).add(qr);
                }
                if (rank[qr] == rank[pr]){
                    rank[pr]++;
                }
            }
        }
    }

    public void two(Integer p, Integer q){
        Integer pr = find(p);
        Integer qr = find(q);
        if (pr != qr){
            if (pr == p && num[pr] == 1){ // case 1: p is a root and has no child
                if (!child.containsKey(qr)){
                    child.put(qr, new HashSet<Integer>());
                    child.get(qr).add(p);
                }
                else{
                    child.get(qr).add(p);
                }
                num[qr]++;
                Sum[qr] += p;
                set[p] = qr;
            }
            else if(pr == p){ // case 2: p is a root with child
                Iterator<Integer> ite = child.get(p).iterator();
                Integer New = ite.next();
                set[New] = New;
                while (ite.hasNext()){
                    Integer cur = ite.next();
                    set[cur] = New;
                    if (!child.containsKey(New)){
                        child.put(New, new HashSet<Integer>());
                        child.get(New).add(cur);
                    }
                    else{
                        child.get(New).add(cur);
                    }
                }
                child.put(p, new HashSet<Integer>());
                if (!child.containsKey(qr)) {
                    child.put(qr, new HashSet<Integer>());
                    child.get(qr).add(p);
                }
                else { 
                    child.get(qr).add(p);
                }
                num[qr]++;
                Sum[qr] += p;
                num[New] = num[pr] - 1;
                Sum[New] = Sum[pr] - p;
                set[p] = qr;
            }
            else{ // case 3: p is not root
                if (child.containsKey(p)){
                    Iterator<Integer> ite= child.get(p).iterator();
                    while (ite.hasNext()){
                        Integer cur = ite.next();
                        set[cur] = pr;
                        if (!child.containsKey(pr)){
                            child.put(pr, new HashSet<Integer>());
                            child.get(pr).add(cur);
                        }
                        else{
                            child.get(pr).add(cur);
                        }
                    }
                }
                child.get(pr).remove(p);
                if (!child.containsKey(qr)) {
                    child.put(qr, new HashSet<Integer>());
                    child.get(qr).add(p);
                }
                else {
                    child.get(qr).add(p);
                }
                num[pr]--;
                Sum[pr] -= p;
                num[qr]++;
                Sum[qr] += p;
                set[p] = qr;
            }
        }
    }

    public Integer[] three(Integer p){
        Integer pr = find(p);
        Integer[] temp = new Integer[2];
        temp[0] = num[pr];
        temp[1] = Sum[pr];

        return temp;
    }
    
}

class Kattio extends PrintWriter {
    public Kattio(InputStream i) {
        super(new BufferedOutputStream(System.out));
        r = new BufferedReader(new InputStreamReader(i));
    }
    public Kattio(InputStream i, OutputStream o) {
        super(new BufferedOutputStream(o));
        r = new BufferedReader(new InputStreamReader(i));
    }

    public boolean hasMoreTokens() {
        return peekToken() != null;
    }

    public int getInt() {
        return Integer.parseInt(nextToken());
    }

    public double getDouble() {
        return Double.parseDouble(nextToken());
    }

    public long getLong() {
        return Long.parseLong(nextToken());
    }

    public String getWord() {
        return nextToken();
    }



    private BufferedReader r;
    private String line;
    private StringTokenizer st;
    private String token;

    private String peekToken() {
        if (token == null)
            try {
                while (st == null || !st.hasMoreTokens()) {
                    line = r.readLine();
                    if (line == null) return null;
                    st = new StringTokenizer(line);
                }
                token = st.nextToken();
            } catch (IOException e) { }
        return token;
    }

    private String nextToken() {
        String ans = peekToken();
        token = null;
        return ans;
    }
}
