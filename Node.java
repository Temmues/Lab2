
package spell;
public class Node implements INode
{
    private int frequency = 0;
    private Node[] children = new Node[26];     // 26 letters

    @Override
    public int getValue() {
        return frequency;
    }

    public void incrementFrequency()
    {
        frequency++;
    }

    public Node find(String word)
    {
        // first check if the word length is zero if so return this we are done
        if(word.length() == 0)
        {
            if (this.frequency > 0)
            {
                return this;
            }
            return null;
        }
        int index = Math.abs('a' - word.charAt(0));
        StringBuilder removeLast = new StringBuilder(word);
        if (children[index] == null)
        {
            return null;
        }
        removeLast.deleteCharAt(0);
        if (children[index].find(removeLast.toString()) == null)
        {
            return null;
        }
        return null;
    }

    public int nearestKids()
    {
        int hashCount = 3;
        for(int i = 0; i < 26; i++)
        {
            if(children[i] != null)
            {
                hashCount += children[i].nearestKids();
            }
        }
        return hashCount;
    }
    public Node[] getKids()
    {
        return children;
    }
}
