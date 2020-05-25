package Default.Bezorgers;
import Default.Entiteit.Routes;
import Default.Login;
import Default.Planners.BezorgerRoutesInzien;
import SQL.SQLFuncties;
import TSP.Route;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class BezorgerRoutes extends JFrame implements ActionListener  {

    private SQLFuncties sql = new SQLFuncties();

    JButton jb = new JButton("Route afronden");
    JLabel routeTitle = new JLabel("Routes voor: "+Login.voornaam + " " + Login.achternaam);
    private Routes route;
    JList<String> list = new JList<>(sql.inzienRouteBijBezorger(Login.acc_id));

    public BezorgerRoutes() throws SQLException {
        super("Route inzien voor: "+Login.voornaam);
        setLayout(new FlowLayout());
        JPanel routepanel = new JPanel(new BorderLayout());
        jb.addActionListener(this);

        list.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()) {
                    route = (Routes) ((JList) e.getSource())
                            .getSelectedValue();
                    System.out.println("Selected id: " + route.getId());
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2) {
                    try {
                        BezorgerRoutesInzien b = new BezorgerRoutesInzien(route.getId());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
        });

        routeTitle.setLabelFor(list);
        routepanel.add(routeTitle, BorderLayout.NORTH);
        routepanel.add(new JScrollPane(list));
        add(routepanel, BorderLayout.WEST);
        add(jb);

        setSize(new Dimension(450, 450));
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb) {
            try {
                int a = JOptionPane.showConfirmDialog(this, "Weet je zeker dat je Route "+route.getId()+" wilt afronden?", "Route "+route.getId(), JOptionPane.OK_CANCEL_OPTION);
                if(a == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(this,"Route "+route.getId()+" is afgerond!");
                    sql.updateRoute(route.getId());
                    dispose();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}