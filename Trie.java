package spell;

public class Trie implements ITrie {

    private int frequency = 0;
    private int wordCount = 0;
    private int nodeCount = 1;
    private int hashNum = 0;// we will always have a min of 1
    private Node root = new Node();
    private Trie[] children = new Trie[26];     // 26 letters in the alphabet
    boolean equivalent = false;

    @Override
    public int hashCode()  // might have to fix you
    {
        boolean hadKids = false;
        int total  = 0;
        StringBuilder builder = new StringBuilder();
        int runningTotal = 0;
        for (int i = 0; i < 26; i++)
        {
            if (root.getKids()[i] != null)
            {
                int cindex = i + 'a';
                runningTotal += cindex;
                hadKids = true;
            }
        }
        if(!hadKids)
        {
            runningTotal = 6;
        }
        //constructHash(root, builder);
        if (wordCount != 0)
        {
            total = runningTotal / frequency - nodeCount;
        }
        else
        {
            total = (runningTotal - frequency)  * nodeCount;
        }
        return total;
    }

    public void constructHash(Node n, StringBuilder current)
    {
        if(n == null)
        {
            return;
        }
        if(n.getValue() > 0)
        {
            hashNum += current.toString().hashCode();
            current.delete(0,current.length() - 1);
        }
        Node[] kids = n.getKids();
        for (int i = 0; i < 26; i++)
        {
            Node child = kids[i];
            if (child != null)
            {
                int cIndex = i + 'a';
                char c = (char) cIndex;
                current.append(c);
                constructHash(child, current);
            }
        }
    }
    @Override
    public boolean equals(Object obj) {
        if(obj == null)
        {
            return false;
        }
        if(obj == this)
        {
            return true;
        }
        if(obj.getClass() == this.getClass())
        {
            Trie checkTrie = (Trie)obj;
            boolean checkBool = checker(checkTrie.getRoot(), root);
            return checkBool;
        }
        return false;
    }

    public boolean checker(Node obj, Node main) // FIX ME!!
    {
        boolean equal = false;
        if(main == null && obj == null)
        {
            return false;
        }
        Node kids[] = obj.getKids();
        Node children[] = main.getKids();
        for (int i = 0; i < 26; i++)
        {
            Node current = kids[i];
            Node compare = children[i];
            if((children[i] != null && kids[i] == null) || (children[i] == null && kids[i] != null)) // check for if one is null and the other is not
            {
                return false;
            }
            if(compare!= null && current != null)
            {
                if(compare.getValue() != current.getValue())
                {
                    return false;
                }
                else if (checker(compare,current) == false)
                {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder current = new StringBuilder();
        StringBuilder output = new StringBuilder();
        reader(root,current,output);
        return output.toString();
    }

    private void reader(Node n, StringBuilder current, StringBuilder output)
    {
        if(n == null)
        {
            return;
        }
        if(n.getValue() > 0)
        {
            output.append(current + "\n");
        }
        Node[] kids = n.getKids();
        for (int i = 0; i < 26; i++)
        {
            Node child = kids[i];
            if (child != null)
            {
                int cIndex = i + 'a';
                char c = (char) cIndex;
                current.append(c);
                reader(child,current,output);
                current.deleteCharAt(current.length() - 1);
            }
        }
    }

    public INode findHelper(Node n, StringBuilder word)
    {
        if (n == null)
        {
            return null;
        }
        if (word.length() == 0)
        {
            if (n.getValue() > 0)
            {
                return n;
            }
            else
            {
                return null;
            }
        }
        int index = Math.abs('a' - word.charAt(0));
        word.deleteCharAt(0);
        Node[] kids = n.getKids();
        if (kids[index] != null)
        {
            return(findHelper(kids[index],word));
        }
        return null;
    }

    @Override
    public void add(String word)
    {
        addNodes(word, root);
    }

    public void addNodes(String word, Node n)
    {
        if(word.length() == 0)
        {
            if(n.getValue() == 0)
            {
                hashNum += n.hashCode();
                frequency++;
            }
            n.incrementFrequency();
            return;
        }
        int index = Math.abs('a' - word.charAt(0));
        Node[] children = n.getKids();
        if(children[index] == null)
        {
            Node kid = new Node();
            children[index] = kid;
            nodeCount++;
        }
        StringBuilder removeTop = new StringBuilder(word);
        removeTop.deleteCharAt(0);
        addNodes(removeTop.toString(), children[index]); // recursive call
        return;
    }
    @Override
    public INode find(String word)
    {
        //Node returnNode = new Node();
        StringBuilder newString = new StringBuilder(word);
        return findHelper(root,newString);
    }

    public void findWords(Node n, int totalWords)
    {
        Node[] kids = n.getKids();
        for (int i = 0; i < 26; i++)
        {
            Node child = kids[i];
            if (child != null)
            {
                findWords(child, totalWords);
            }
        }
        totalWords += n.getValue();
    }

    @Override
    public int getWordCount()
    {
        return frequency;
    }


    @Override
    public int getNodeCount()
    {
        return nodeCount;
    }

    public Node getRoot()
    {
        return root;
    }
    public void clear()
    {
        children = null;
    }
}
