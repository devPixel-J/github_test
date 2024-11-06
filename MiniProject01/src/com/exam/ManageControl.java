package com.exam;

import java.sql.*;
import java.util.Scanner;

public class ManageControl {
    String url = "jdbc:mariadb://localhost:3306/sample";
    String user = "root";
    String password = "!123456";

    Connection conn = null;
    Statement stmt = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt1 = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs = null;

    //Scanner sc = new Scanner(System.in);



    public ManageControl() {

    }

    public void startMenu(){

        Scanner sc = new Scanner(System.in);

        int menuNum ;

        System.out.println("                             회원 관리 프로그램                            ");
        System.out.println("=========================================================================");
        System.out.println("1. 회원정보목록");
        System.out.println("2. 회원정보등록");
        System.out.println("3. 회원정보수정");
        System.out.println("4. 회원정보삭제");


        System.out.println("5. 쪽지 보내기");

        System.out.println("6. 종료");
        System.out.println("=========================================================================");
        System.out.print("메뉴를 입력하세요 : ");

        menuNum = sc.nextInt();
        selectMenu(menuNum);

    }



    public void selectMenu(int num) {


        switch (num) {
            case 1:
                showCulumn();
                break;
            case 2:
                memberInsert();
                break;
            case 3:
                memberModify();
                break;
            case 4:
                memberDelete();
                break;
            case 5:
                sendMessage();
                break;
            case 6:
                //sc.close();
                System.out.println("종료");
                break;
            default:
                startMenu();


        }
    }


    public void showCulumn() {

        String sql = "select * from member";

        try {
            // 동적으로 클래스 로딩
            Class.forName("org.mariadb.jdbc.Driver");
            // DriverManager 를 통한 커넥션
            conn = DriverManager.getConnection(url, user, password);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int num = rs.getInt("num");
                String name = rs.getString("name");
                String mail = rs.getString("mail");
                String phone = rs.getString("phone");
                String community = rs.getString("community");
                String birth = rs.getString("birth");
                String regdate = rs.getString("regdate");
                System.out.printf("%s %s %s %s %s %s %s%n", num, name, phone, mail, community, birth, regdate);
            }

        } catch (ClassNotFoundException e) {
            System.out.println("[에러] : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[에러] : " + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }

            startMenu();

        }

    }

    public void memberInsert() {

        Scanner sc = new Scanner(System.in);

        System.out.println("등록할회원정보를 입력해주세요");
        System.out.print("▶▶ 이름 :");
        String name = sc.nextLine();
        System.out.print("▶▶ 연락처 :");
        String phone = sc.nextLine();
        System.out.print("▶▶ 이메일 :");
        String mail = sc.nextLine();
        System.out.print("▶▶ 그룹 :");
        String community = sc.nextLine();
        System.out.print("▶▶ 생년월일 :");
        String birth = sc.nextLine();

        String sql = "INSERT INTO member (name, phone, mail, community, birth  )VALUES ( ?, ?, ?, ?,? )";
        try {
            // 동적으로 클래스 로딩
            Class.forName("org.mariadb.jdbc.Driver");
            // DriverManager 를 통한 커넥션
            conn = DriverManager.getConnection(url, user, password);
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, mail);
            pstmt.setString(4, community);
            pstmt.setString(5, birth);
            System.out.println("회원정보를 등록하시겠습니까?");
            String doInsert = sc.nextLine();
            if (doInsert.equals("y")) {
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    System.out.println("회원정보를 정상적으로 등록했습니다.");
                } else {
                    System.out.println("회원정보등록에 실패 했습니다.");
                }
            }

        } catch (ClassNotFoundException e) {
            System.out.println("[에러] : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[에러] : " + e.getMessage());
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
            startMenu();
        }
    }

    public void memberModify() {

        Scanner sc = new Scanner(System.in);

        System.out.print("수정할 회원의 등록번호를 입력해주세요 ");
        int num = sc.nextInt();
        sc.nextLine(); // 개행 문자 소비
        String sql1 = "select * from member where num = ? ";
        String sql2 = "update member set name =?, phone =?, mail =?, community=?, birth=? where num = ?   ";
        try {
            String name ="";
            String mail = "";
            String phone ="";
            String community = "";
            String birth = "";
            // 동적으로 클래스 로딩
            Class.forName("org.mariadb.jdbc.Driver");
            // DriverManager 를 통한 커넥션
            conn = DriverManager.getConnection(url, user, password);
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, num);
            System.out.println("select * from member where num ="+num);
            rs = pstmt1.executeQuery();

            while (rs.next()) {
                name = rs.getString("name");
                mail = rs.getString("mail");
                phone = rs.getString("phone");
                community = rs.getString("community");
                birth = rs.getString("birth");
            }

            System.out.printf("[ %s ] 님의 회원정보%n", num);
            System.out.printf("%s %s %s %s %s%n", name, phone, mail, community, birth);

            System.out.println("회원정보수정을 계속하시겠습니까 (y/n)");
            String doModify =  sc.nextLine();
            if (doModify.equals("y")) {
                System.out.println("입력하지 않으면 기존의 정보가 그대로 유지됩니다.");
                pstmt2 = conn.prepareStatement(sql2);
                System.out.print("▶▶ 이름 :");
                String mName = sc.nextLine() ;
                name = mName.trim() ==""  ? name : mName  ;
                System.out.print("▶▶ 연락처 :");
                String mPhone = sc.nextLine();
                phone = mPhone.trim()=="" ? phone : mPhone  ;
                System.out.print("▶▶ 이메일 :");
                String mMail = sc.nextLine() ;
                mail = mMail.trim()=="" ? mail : mMail;
                System.out.print("▶▶ 그룹 :");
                String mCommunity = sc.nextLine();
                community = mCommunity.trim()=="" ? community : mCommunity;
                System.out.print("▶▶ 생년월일 :");
                String mBirth = sc.nextLine();
                birth = mBirth.trim()=="" ? birth : mBirth;

                pstmt2.setString(1,name);
                pstmt2.setString(2,phone);
                pstmt2.setString(3,mail);
                pstmt2.setString(4,community);
                pstmt2.setString(5,birth);
                pstmt2.setInt(6,num);

                int result = pstmt2.executeUpdate();
                if (result > 0) {
                    System.out.println("회원정보를 정상적으로 수정하였습니다. ");
                } else {
                    System.out.println("회원정보 수정에 실패했습니다. ");
                }
            }


        } catch (ClassNotFoundException e) {
            System.out.println("[에러] : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("[에러] : " + e.getMessage());
        } finally {
            if (pstmt2 != null) {
                try {
                    pstmt2.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt1 != null) {
                try {
                    pstmt1.close();
                } catch (SQLException e) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
            startMenu();
        }
    }

    public void memberDelete() {
        Scanner sc = new Scanner(System.in);

        System.out.print("삭제할 회원의 등록번호를 입력해주세요.");
        int num = sc.nextInt();
        String sql1 = "select * from member where num = ?";
        String sql2 = "delete from member where num = ?" ;

        try {
            Class.forName("org.mariadb.jdbc.Driver");
            // DriverManager 를 통한 커넥션
            conn = DriverManager.getConnection(url, user, password);
            pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, num);
            rs = pstmt1.executeQuery();
            int rowCount = 0;
            while (rs.next()) {
                rowCount ++ ;
            }
            if (rowCount == 0) {
                System.out.println("입력하신 회원등록번호에 해당하는 회원은 존재하지 않습니다.");
            }else {
                pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setInt(1,num);

                int result = pstmt2.executeUpdate();
                if (result > 0) {
                    System.out.println("회원정보를 정상적으로 삭제했습니다.");
                } else {
                    System.out.println("회원정보 삭제에 실패했습니다. ");
                }
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (pstmt2 != null) {
                try {
                    pstmt2.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt1 != null) {
                try {
                    pstmt1.close();
                } catch (SQLException e) {
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
            startMenu();
        }
    }

    public void sendMessage() {

    }


}
