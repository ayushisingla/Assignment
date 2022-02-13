import java.util.*;

public class RoleHierarchy
{
    static Node root = null;
    static Map<String, ArrayList<String>> map;

    // Represents a node of an n-ary tree
    static class Node
    {
        String key;
        Vector<Node>child = new Vector<>();
    };

    // Utility function to create a new tree node
    static Node newNode(String key)
    {
        Node temp = new Node();
        temp.key = key;
        return temp;
    }

    // 1. Add subrole
    static void addSubRole(String subRole, String reportingRole){
        Node reportingRoleNode = findReportingRole(reportingRole);
        if(reportingRoleNode==null){
            System.out.println("Reporting Role not found.");
            return;
        }
        ArrayList<String> list = new ArrayList<>();
        map.put(subRole, list);
        reportingRoleNode.child.add(newNode(subRole));
    }

    // 1. Add subrole - Find Reporting role ( Simply find Node using String provided as parameters)
    static Node findReportingRole(String reportingRole){
        Queue<Node > q = new LinkedList<>(); // Create a queue
        q.add(root); // Enqueue root
        while (!q.isEmpty())
        {
            int n = q.size();
            while (n > 0) // If node has children
            {
                Node p = q.peek();// Dequeue an item from queue
                if (p.key.equals(reportingRole)) return p;
                q.remove();
                // Enqueue all children of the dequeued item
                for (int i = 0; i < p.child.size(); i++)
                    q.add(p.child.get(i));
                n--;
            }
        }
        return null;
    }

    // 2. Prints the n-ary tree level wise
    static void levelOrderTraversal(Node root)
    {
        if (root == null)
            return;
        Queue<Node > q = new LinkedList<>(); // Create a queue
        q.add(root); // Enqueue root
        while (!q.isEmpty())
        {
            int n = q.size();
            while (n > 0) // If this node has children
            {
                // Dequeue an item from queue and print it
                Node p = q.peek();
                q.remove();
                System.out.print(p.key + " ");
                // Enqueue all children of the dequeued item
                for (int i = 0; i < p.child.size(); i++)
                    q.add(p.child.get(i));
                n--;
            }
            // Print new line between two levels
            System.out.println();
        }
    }

    // 3. Delete role
    static void deleteRole(String roleToBeDeleted, String roleToBeTransferred)
    {
        // Let's find parent of deletedNode and child of deletedNode
        // add childs of deletedNode to transferNode
        // add users from deletedNde to transferNode
        // set parent ref to  null afterwards

        Queue<Node> q = new LinkedList<>(); // Create a queue
        q.add(root); // Enqueue root
        while (!q.isEmpty())
        {
            int n = q.size();
            while (n > 0)
            {
                Node p = q.peek();
                q.remove();
                for (int i = 0; i < p.child.size(); i++)
                {
                    if(p.child.get(i).key.equals(roleToBeDeleted))
                    {
                        // p becomes parent
                        // p.child.get(i) -> node to be deleted
                        Node nodeToBeDeleted = p.child.get(i);
                        Node roleToBeTransferredNode =  findReportingRole(roleToBeTransferred);
                        // Step1: Transfer child nodes
                        for (int j = 0; j < nodeToBeDeleted.child.size(); j++)
                            roleToBeTransferredNode.child.add(nodeToBeDeleted.child.get(j));

                        //Step2: assign users of deletedNode to transfer Node
                        ArrayList<String> deletedNodeUsers = map.get(nodeToBeDeleted.key);
                        ArrayList<String> roleToBeTransferredUsers = map.get(roleToBeTransferredNode.key);
                        roleToBeTransferredUsers.addAll(deletedNodeUsers);
                        map.remove(nodeToBeDeleted.key);

                        //Step3: Delete the node
                        p.child.remove(i);
                        return;
                    }    
                    q.add(p.child.get(i));
                }
                n--;
            }
        }
    }

    // 4. Add user
    static void addUser(String userName,String role)
    {
        //Traversing Map  
        ArrayList<String> list = new ArrayList<>();
        for(Map.Entry<String, ArrayList<String>> m:map.entrySet()){  
            if(m.getKey().equals(role))
            {
                list = (ArrayList<String>) m.getValue();
                list.add(userName);
                map.put(role, list);
                break;
            } 
        }   
    }

    // 5. Display Users
    static void displayUsers()
    {
        //Traversing Map 
        List<String> list = new ArrayList<>();
        for(Map.Entry<String, ArrayList<String>> m:map.entrySet()){  
            list = (List<String>) m.getValue();
            for(int i=0;i<list.size();i++)
            {
                System.out.println(list.get(i)+" - "+m.getKey());
            }
        } 
    }

    // 6. Display Users and Sub Users
    static void displayUsersAndSubUsers()
    {
        // 1. Traverse String key
        // 2. For each key traverse list
        // 3. For each list value- traverse below keys
        // 4. and print user names
        Queue<Node > q = new LinkedList<>(); // Create a queue
        q.add(root); // Enqueue root
        while (!q.isEmpty())
        {
            int n = q.size();
            while (n > 0)
            {
                Node p = q.peek();

                // To do with key logic comes here:
                ArrayList<String> list = map.get(p.key);
                for(int i=0;i<list.size();i++)
                {
                    String user = list.get(i);
                    ArrayList<String> subUsersList = getSubUsers(p);
                    
                    System.out.print(user+" - ");
                    for(int j=0;j<subUsersList.size();j++)
                    {
                        System.out.print(subUsersList.get(j));
                        if(j!=subUsersList.size()-1)
                        {
                            System.out.print(",");
                        }
                    }
                    System.out.println();
                }
                q.remove();

                // Enqueue all children of the dequeued item
                for (int i = 0; i < p.child.size(); i++)
                    q.add(p.child.get(i));
                n--;
            }
        }
    }

    // 6. Display Users and Sub Users - Get subUsers
    static ArrayList<String> getSubUsers(Node userRoot){
        ArrayList<String> list = new ArrayList<>();
        // 1. Add subRoles to queue
        // 2. LOT -> add users
        Queue<Node> q = new LinkedList<>(); // Create a queue
        q.add(userRoot); // Enqueue root
        boolean temp=true;
        while (!q.isEmpty())
        {
            int n = q.size();
            // If this node has children
            while (n > 0)
            {
                // Dequeue an item from queue
                Node p = q.peek();
                q.remove();
                if(temp)
                {
                    temp=false;
                }
                else{
                    ArrayList<String> sublist = map.get(p.key);
                    list.addAll(sublist);
                }
                // Enqueue all children of the dequeued item
                for (int i = 0; i < p.child.size(); i++)
                    q.add(p.child.get(i));
                n--;
            }
        }
        return list;
    }
    
    // 7. Delete User
    static void deleteUsername(String deleteUsername)
    {
        // 1. Traverse String key
        // 2. For each key traverse list
        // 3. If found, remove, and break

        Queue<Node > q = new LinkedList<>(); // Create a queue
        q.add(root); // Enqueue root
        while (!q.isEmpty())
        {
            int n = q.size();
            while (n > 0)
            {
                // Dequeue an item from queue
                Node p = q.peek();
                q.remove();

                // To do with key logic comes here:
                ArrayList<String> list = map.get(p.key);
                if(list.contains(deleteUsername))
                {
                    list.remove(deleteUsername);
                }

                // Enqueue all children of the dequeued item
                for (int i = 0; i < p.child.size(); i++)
                    q.add(p.child.get(i));
                n--;
            }
        }
    }

    // Driver Code
    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter root role name :");
        String rootVal = sc.nextLine().toLowerCase();
        map = new HashMap<String,ArrayList<String>>();
        map.put(rootVal,new ArrayList<String>());
        root = newNode(rootVal);
        boolean runApp = true;
        while(runApp)
        {
            System.out.println();
            System.out.println("\n Operations : \n 1. Add Sub Role. \n 2. Display Roles \n 3. Delete Role. \n 4. Add User. \n 5. Display Users. \n 6. Display Users and Sub Users.\n 7. Delete User.\n 8. Number of users from top \n 9. Height of role hierachy. \n 10. Common boss of users");
            System.out.println();
            System.out.print("Operation to be performed :");
            int input = sc.nextInt();
            sc.nextLine();
            switch(input)
            {
            case 1: // Add Sub Role
                {
                    System.out.print("Enter sub role name : ");
                    String subrole = sc.nextLine().toLowerCase();
                    System.out.print("Enter reporting to role name : ");
                    String reportingRole = sc.nextLine().toLowerCase();
                    System.out.println();
                    addSubRole(subrole,reportingRole);
                    break;
                }
            case 2: // Display Roles.
                {
                    levelOrderTraversal(root);
                    break;
                }
            case 3: // Delete Role
                {
                    System.out.print("Enter the role to be deleted : ");
                    String roleToBeDeleted = sc.nextLine().toLowerCase();
                    System.out.print("Enter the role to be transferred : ");
                    String roleToBeTransferred  = sc.nextLine().toLowerCase();
                    System.out.println();
                    deleteRole(roleToBeDeleted,roleToBeTransferred);
                    break;
                }
            case 4: // Add User.
                {
                    System.out.print("Enter User Name : ");
                    String userName = sc.nextLine().toLowerCase();
                    System.out.print("Enter Role : ");
                    String role  = sc.nextLine().toLowerCase();
                    System.out.println();
                    addUser(userName,role);
                    break;
                }
            case 5: // Display Users.
                {
                    displayUsers();
                    break;
                }
            case 6: // Display Users and Sub Users
                {
                    displayUsersAndSubUsers();
                    break;
                }
            case 7: // Delete User
                {
                    System.out.print("Enter username to be deleted : ");
                    String deleteUsername  = sc.nextLine().toLowerCase();
                    System.out.println();
                    deleteUsername(deleteUsername);
                    break;
                }
            case 8: // Number of users from top. (Uncompleted)
                {
                    System.out.print("Enter user name  : ");
                    String userName  = sc.nextLine();
                    System.out.println();
                    
                    //output
                    int numOfUsersFromTop =0;//just for test
                    System.out.println("Number of users from top  : "+ numOfUsersFromTop);
                    break;
                }
            case 9: // Height of role hierachy. (Uncompleted)
                {
                    int height =0; //just for test
                    System.out.println("height  : "+ height);
                    break;
                }
            case 10: // Common boss of users (Uncompleted)
                {
                    System.out.print("Enter user 1  : ");
                    String user1  = sc.nextLine();
                    System.out.println();
                    System.out.print("Enter user 2  : ");
                    String user2  = sc.nextLine();
                    System.out.println();
                    //output
                    String topCommonBoss = ""; //just for test
                    System.out.println("Top most common boss  : "+topCommonBoss);
                    break;
                }
            default:
                {
                    runApp = false;
                    break;
                }
            
            }
        }
        sc.close();
    }
}