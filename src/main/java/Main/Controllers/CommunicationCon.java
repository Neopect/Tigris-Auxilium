package Main.Controllers;

import Main.Features.SQLConnection;
import Main.Features.tableCons.ChatCL;
import Main.Features.tableCons.ChatCLUser;
import Main.Features.tableCons.UsersL;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommunicationCon {
    public TableView<ChatCL> chatTable;
    public TableColumn<ChatCL, String> chatTableUsernameCol;
    public TableColumn<ChatCL, String> chatTableMessageCol;
    public TableColumn<ChatCL, Integer> chatTableTimeCol;
    public TableColumn<ChatCL, Integer> chatTableIDCol;

    public TextField chatSendTf;

    public CheckBox chatAutoRefCB;

    public TextField settingInfoTf;
    public Label settingInfoLbl;

    SQLConnection Con = new SQLConnection();

    List<Integer> listid = new ArrayList<Integer>(); //User list of id
    List<String> listName = new ArrayList<String>();//User list of name
    List<String> listUsername = new ArrayList<String>();// List of full username

    List<String> listMid = new ArrayList<String>();//List of replaced id-to-username

    List<Integer> rsUserid = new ArrayList<Integer>();
    List<String> rsMessage = new ArrayList<String>();
    List<Integer> rsTime = new ArrayList<Integer>();
    List<Integer> rsID = new ArrayList<Integer>();

    String localname;
    String localID;


    public void initialize() {
        //SqliteConnection sqlConn = new SqliteConnection();
        //connection = sqlConn.dbConnector("ProjectPlanner");

        //showProject();
        localID = "991";
        localname = "Ty";
        settingInfoLbl.setText("#" + localID);
        settingInfoTf.setText(localname);

    }


    public void onActionChatSendBtn(ActionEvent actionEvent) {

    }

    public void onActionSettingsChangeBtn(ActionEvent actionEvent) {
    }

    public void onActionChatRefreshBtn(ActionEvent actionEvent) {
        getUserList();
        showMessages();
    }

    public void onActionChatAutoRefBtn(ActionEvent actionEvent) {
        /*
        getUserList();
        getChatList();
        System.out.println(listid);
        System.out.println(listName);
        System.out.println(listid.size());
        //attachID();
        //*/
        //listTest();
        //replaceNames();
        //loadNames();
        String ert = (java.time.LocalTime.now()).toString();
        //TODO remove symbols and num to make an integer
        System.out.println(ert);

    }

    public ObservableList<ChatCL> getChatList(){ //--------------------------------------------------
        ObservableList<ChatCL> ChatList = FXCollections.observableArrayList();
        //Connection conn = getConnection();
        Connection conn = Con.sqliteCon("test");
        String query = "select * from Messages";
        Statement st = null; //Use prepare statement for repetitive scripts
        ResultSet rs = null;

        /*
        List<Integer> rsUserid = new ArrayList<Integer>();
        List<String> rsMessage = new ArrayList<String>();
        List<Integer> rsTime = new ArrayList<Integer>();
        List<Integer> rsID = new ArrayList<Integer>();

         */

        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            ChatCL chatCL;
            //int dataindex= 0;
            while(rs.next()) {//This will pack id list then
                //chatCL = new ChatCL(rs.getString("user"), rs.getInt("userID"), rs.getString("message"), rs.getInt("Time"), rs.getInt("ID"));
                //ChatList.add(chatCL);


                listMid.add(String.valueOf(rs.getInt("userID")));
                System.out.println("Added value " + rs.getInt("userID") + " to array");
                //dataindex++;

                rsUserid.add(rs.getInt("userid"));
                rsMessage.add(rs.getString("message"));
                rsTime.add(rs.getInt("Time"));
                rsID.add(rs.getInt("ID"));



            }
            /*
            Then convert it
             */

            for(int x = 0; x< listid.size(); x++) {
                //Collections.replaceAll(list3, (list2.get(x)).toString(), (list.get(x)).toString());
                try {
                    Collections.replaceAll(listMid, (listid.get(x)).toString(), (listName.get(x)).toString()+"#"+(listid.get(x)).toString());
                    System.out.println(listMid.get(x));
                    //nameList.add(chatclu);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
            System.out.println("New name list: " + listMid);


            /*
            The save everything
             */



            for(int x = 0; x< listMid.size(); x++) {
                chatCL = new ChatCL(listMid.get(x), rsUserid.get(x), rsMessage.get(x), rsTime.get(x), rsID.get(x));
                ChatList.add(chatCL);
            }

            /*
            Spacer
             */


        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return ChatList;
    }


    public void getUserList(){ //--------------------------------------------------
        ObservableList<UsersL> UserList = FXCollections.observableArrayList();
        //Connection conn = getConnection();
        Connection conn = Con.sqliteCon("test");
        String query = "select * from Users";
        Statement st = null; //Use prepare statement for repetitive scripts
        ResultSet rs = null;


        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            UsersL usersL;
            while(rs.next()) {
                usersL = new UsersL(rs.getString("name"), rs.getInt("id"));
                UserList.add(usersL);
                listName.add(rs.getString("name"));
                //listid.add(String.valueOf(rs.getInt("id")));
                listid.add(rs.getInt("id"));
            }

        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }


    public void showMessages() { //--------------------------------------------------
        ObservableList<ChatCL> list = getChatList();

        chatTableUsernameCol.setCellValueFactory(new PropertyValueFactory<ChatCL, String>("username"));
        //chatTableUsernameCol.setCellValueFactory(new PropertyValueFactory<ChatCLUser, String>("usernid"));
        chatTableMessageCol.setCellValueFactory(new PropertyValueFactory<ChatCL, String>("message"));
        chatTableTimeCol.setCellValueFactory(new PropertyValueFactory<ChatCL, Integer>("time"));
        chatTableIDCol.setCellValueFactory(new PropertyValueFactory<ChatCL, Integer>("messageID"));

        chatTable.setItems(list);

    }


    private void executeQuery(String query) {
        //Connection conn = getConnection();

        Connection conn = Con.sqliteCon("test");

        Statement st = null;
        try {
            st = conn.createStatement();
            st.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();

        }finally {
            try {
                st.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /*
    private void insertRecord() {
        int msgIdLength = rsID.size() + 2;

        String query = "INSERT INTO "+"Messages"+" (ID,User,userID,Message,Time) VALUES ("+msgIdLength+",'"+ localname + "','" + localID + "','"+chatSendTf+"',"++")";
        executeQuery(query);
        showTabs();
    }

    private void updateRecord() {
        String query = "UPDATE "+tabLv.getSelectionModel().getSelectedItem()+" SET name = '" + tabTfName.getText() + "', url = '" + tabTfUrl.getText() + "' WHERE name = '" + tabTfName.getText() + "' OR url = '" +tabTfUrl.getText()+ "'";
        executeQuery(query);
        showTabs();
    }

    private void deleteRecord() {
        String query = "DELETE FROM "+tabLv.getSelectionModel().getSelectedItem()+" WHERE name = '" + tabTfName.getText() + "'";
        executeQuery(query);
        showTabs();
    }
    //*/



    /*
    -----------------------------------------------------------END OF SQL-----------------------------------------------------------------------------
     */

    void attachID() {
        int i;
        for(int x = 0; x< listid.size(); x++) {
            String log = listName.get(x) +"#" + (listid.get(x)).toString();
            listUsername.add(log);
        }
        System.out.println(listUsername);

    }


    void loadNames() { //DEP
        ObservableList<ChatCLUser> nameList = FXCollections.observableArrayList();
        System.out.println("Old list: "+listMid);
        ChatCLUser chatclu;
        for(int x = 0; x< listid.size(); x++) {
            //Collections.replaceAll(list3, (list2.get(x)).toString(), (list.get(x)).toString());
            try {
                Collections.replaceAll(listMid, (listid.get(x)).toString(), (listName.get(x)).toString()+"#"+(listid.get(x)).toString());
                System.out.println(listMid.get(x));
                chatclu = new ChatCLUser(listMid.get(x));
                //nameList.add(listMid.get(x));
                nameList.add(chatclu);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        System.out.println("New list: " + listMid);
    }


    void listTest() { //TESTING PUR
        List<String> list = new ArrayList<String>();
        list.add("Java");
        list.add("Java Script");
        list.add("HBase");
        list.add("CoffeeScript");
        //list.add("Java");
        list.add("TypeScript");
        System.out.println("Contents of list: "+list);
        Collections.replaceAll(list, "Java", "JAVA");

        System.out.print("Contents of list after replace operation: \n"+list);

        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(1);
        list2.add(234);
        list2.add(2344);
        list2.add(1233);
        //list2.add(1);
        list2.add(54);


        List<String> list3 = new ArrayList<String>();
        list3.add("1");
        list3.add("234");
        list3.add("54");
        list3.add("1233");
        list3.add("1233");
        list3.add("1");


        for(int x = 0; x< listid.size(); x++) {
            //Collections.replaceAll(list3, (list2.get(x)).toString(), (list.get(x)).toString());
            try {
                Collections.replaceAll(list3, (list2.get(x)).toString(), (list.get(x)).toString());
            }catch (Exception e){

            }

        }

        System.out.println("modifed list" + list3);
    }

    void replaceNames() { //TESTING PUR
        List<String> list = new ArrayList<String>();
        list.add("Java");
        list.add("Java Script");
        list.add("HBase");
        list.add("CoffeeScript");
        //list.add("Java");
        list.add("TypeScript");
        System.out.println("Contents of list: "+list);

        List<Integer> list2 = new ArrayList<Integer>();
        list2.add(1);
        list2.add(234);
        list2.add(2344);
        list2.add(1233);
        //list2.add(1);
        list2.add(54);


        List<String> list3 = new ArrayList<String>();
        list3.add("1");
        list3.add("234");
        list3.add("54");
        list3.add("1233");
        list3.add("1233");
        list3.add("1");


        for(int x = 0; x< listid.size(); x++) {
            //Collections.replaceAll(list3, (list2.get(x)).toString(), (list.get(x)).toString());
            try {
                Collections.replaceAll(list3, (list2.get(x)).toString(), (list.get(x))+"#"+(list2.get(x)).toString());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        System.out.println("Checkup");
        System.out.println("modifed list" + list3);
    }




}

/*
List<Integer> list = new ArrayList<Integer>();
        int i = 0;

        i++;
        list.add(i);
        System.out.println(i);
        System.out.println(list);
 */

/*
public ObservableList<ChatCL> getChatList(){ //--------------------------------------------------
        ObservableList<ChatCL> ChatList = FXCollections.observableArrayList();
        //Connection conn = getConnection();
        Connection conn = Con.sqliteCon("test");
        String query = "select * from Messages";
        Statement st = null; //Use prepare statement for repetitive scripts
        ResultSet rs = null;




        try{
            st = conn.createStatement();
            rs = st.executeQuery(query);
            ChatCL chatCL;
            while(rs.next()) {
                chatCL = new ChatCL(rs.getString("user"), rs.getInt("userID"), rs.getString("message"), rs.getInt("Time"), rs.getInt("ID"));
                ChatList.add(chatCL);
                listMid.add(String.valueOf(rs.getInt("userID")));
            }

        }catch(Exception e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
                st.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return ChatList;
    }
 */