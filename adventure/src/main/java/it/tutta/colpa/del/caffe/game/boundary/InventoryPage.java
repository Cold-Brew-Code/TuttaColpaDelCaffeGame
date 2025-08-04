
package it.tutta.colpa.del.caffe.game.boundary;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.tutta.colpa.del.caffe.game.control.Controller;
import it.tutta.colpa.del.caffe.game.entity.GeneralItem;
import it.tutta.colpa.del.caffe.game.entity.Inventory;
import it.tutta.colpa.del.caffe.game.entity.Item;
import it.tutta.colpa.del.caffe.game.exception.ImageNotFoundException;

public class InventoryPage extends JDialog implements GUI {
    private GeneralItem[] it = new GeneralItem[4];
    private static final Logger logger = Logger.getLogger(InventoryPage.class.getName());

    public InventoryPage(Frame parent, Inventory inventory) {
        super(parent, "Inventario", true);
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        initComponents();
        int i = 0;
        for (GeneralItem iterator : inventory) {
            try {
                switch (i) {
                    case 0:
                        it[0] = iterator;
                        System.out.println(it[0]);
                        System.out.println(iterator);
                        setImage(firstItem, iterator.getImmagine());
                        qtyFirstItem.setText(String.valueOf(inventory.getQuantity(iterator)));
                        break;
                    case 1:
                        it[1] = iterator;
                        setImage(secondItem, iterator.getImmagine());
                        qtySecondItem.setText(String.valueOf(inventory.getQuantity(iterator)));
                        break;
                    case 2:
                        it[2] = iterator;
                        setImage(thirdItem, iterator.getImmagine());
                        qtyThirdItem.setText(String.valueOf(inventory.getQuantity(iterator)));
                        break;
                    case 3:
                        it[3] = iterator;
                        setImage(fourthItem, iterator.getImmagine());
                        qtyFourthItem.setText(String.valueOf(inventory.getQuantity(iterator)));
                        break;
                }
            } catch (ImageNotFoundException e) {
                System.err.println("[Image not found] " + iterator.getImmagine());
            }
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        panel = new JPanel() {
            private final Image wp;

            {
                URL imgUrl = getClass().getResource("/images/zaino_interno.png");
                if (imgUrl != null) {
                    wp = new ImageIcon(imgUrl).getImage();
                } else {
                    wp = null;
                    System.err.println("Immagine non trovata: images/zaino_interno.png");
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (wp != null) {
                    g.drawImage(wp, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        firstItem = new JLabel();
        secondItem = new JLabel();
        thirdItem = new JLabel();
        fourthItem = new JLabel();
        descriptionLabel = new JTextArea();
        qtyFirstItem = new JLabel();
        qtySecondItem = new JLabel();
        qtyThirdItem = new JLabel();
        qtyFourthItem = new JLabel();
        scrollPane = new JScrollPane();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        firstItem.setBackground(new Color(255, 255, 255));
        firstItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        firstItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                firstItemMouseClicked(evt);
            }

            public void mouseEntered(MouseEvent evt) {
                firstItemMouseEntered(evt);
            }

            public void mouseExited(MouseEvent evt) {
                firstItemMouseExited(evt);
            }
        });

        secondItem.setBackground(new Color(255, 255, 255));
        secondItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        secondItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                secondItemMouseClicked(evt);
            }

            public void mouseEntered(MouseEvent evt) {
                secondItemMouseEntered(evt);
            }

            public void mouseExited(MouseEvent evt) {
                secondItemMouseExited(evt);
            }
        });

        thirdItem.setBackground(new Color(255, 255, 255));
        thirdItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        thirdItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                thirdItemMouseClicked(evt);
            }

            public void mouseEntered(MouseEvent evt) {
                thirdItemMouseEntered(evt);
            }

            public void mouseExited(MouseEvent evt) {
                thirdItemMouseExited(evt);
            }
        });

        fourthItem.setBackground(new Color(255, 255, 255));
        fourthItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        fourthItem.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                fourthItemMouseClicked(evt);
            }

            public void mouseEntered(MouseEvent evt) {
                fourthItemMouseEntered(evt);
            }

            public void mouseExited(MouseEvent evt) {
                fourthItemMouseExited(evt);
            }
        });

        descriptionLabel.setText("Seleziona un elemento per conoscerne la descrizione.");
        descriptionLabel.setOpaque(true);
        descriptionLabel.setBackground(new Color(0xF3E28B));
        descriptionLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        descriptionLabel.setFocusable(false);
        descriptionLabel.setEditable(false);
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        scrollPane.setViewportView(descriptionLabel);

        qtyFirstItem.setHorizontalAlignment(SwingConstants.CENTER);
        qtyFirstItem.setOpaque(true);
        qtyFirstItem.setBackground(new Color(0xF3E28B));
        qtyFirstItem.setText("0");
        qtyFirstItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        qtyFirstItem.setHorizontalTextPosition(SwingConstants.CENTER);

        qtySecondItem.setHorizontalAlignment(SwingConstants.CENTER);
        qtySecondItem.setOpaque(true);
        qtySecondItem.setBackground(new Color(0xF3E28B));
        qtySecondItem.setText("0");
        qtySecondItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        qtySecondItem.setHorizontalTextPosition(SwingConstants.CENTER);

        qtyThirdItem.setHorizontalAlignment(SwingConstants.CENTER);
        qtyThirdItem.setOpaque(true);
        qtyThirdItem.setBackground(new Color(0xF3E28B));
        qtyThirdItem.setText("0");
        qtyThirdItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        qtyThirdItem.setHorizontalTextPosition(SwingConstants.CENTER);

        qtyFourthItem.setHorizontalAlignment(SwingConstants.CENTER);
        qtyFourthItem.setOpaque(true);
        qtyFourthItem.setBackground(new Color(0xF3E28B));
        qtyFourthItem.setText("0");
        qtyFourthItem.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255), 4));
        qtyFourthItem.setHorizontalTextPosition(SwingConstants.CENTER);

        GroupLayout panelLayout = new GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(qtyFirstItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(qtySecondItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addGap(87, 87, 87))
                        .addGroup(GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(qtyFourthItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(qtyThirdItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                .addGap(88, 88, 88))
                        .addGroup(panelLayout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(thirdItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(firstItem, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
                                .addGap(60, 60, 60)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(secondItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(fourthItem, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                                .addGap(60, 60, 60))
                        .addGroup(GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                                .addContainerGap())
        );
        panelLayout.setVerticalGroup(
                panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(panelLayout.createSequentialGroup()
                                .addGap(37, 37, 37)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(secondItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(firstItem, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(qtyFirstItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(qtySecondItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(thirdItem, GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                                        .addComponent(fourthItem, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(qtyFourthItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(qtyThirdItem, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)
                                .addContainerGap())
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        this.setResizable(false);
        firstItem.setHorizontalAlignment(SwingConstants.CENTER);
        firstItem.setVerticalAlignment(SwingConstants.CENTER);
        secondItem.setHorizontalAlignment(SwingConstants.CENTER);
        secondItem.setVerticalAlignment(SwingConstants.CENTER);
        thirdItem.setHorizontalAlignment(SwingConstants.CENTER);
        thirdItem.setVerticalAlignment(SwingConstants.CENTER);
        fourthItem.setHorizontalAlignment(SwingConstants.CENTER);
        fourthItem.setVerticalAlignment(SwingConstants.CENTER);
        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>

    private void firstItemMouseEntered(MouseEvent evt) {
        setDescription(1);
    }

    private void firstItemMouseClicked(MouseEvent evt) {
        setDescription(1);
    }

    private void firstItemMouseExited(MouseEvent evt) {

    }

    private void secondItemMouseClicked(MouseEvent evt) {
        setDescription(2);
    }

    private void secondItemMouseEntered(MouseEvent evt) {
        setDescription(2);
    }

    private void secondItemMouseExited(MouseEvent evt) {
    }

    private void thirdItemMouseClicked(MouseEvent evt) {
        setDescription(3);
    }

    private void thirdItemMouseEntered(MouseEvent evt) {
        setDescription(3);
    }

    private void thirdItemMouseExited(MouseEvent evt) {

    }

    private void fourthItemMouseClicked(MouseEvent evt) {
        setDescription(4);
    }

    private void fourthItemMouseEntered(MouseEvent evt) {
        setDescription(4);
    }

    private void fourthItemMouseExited(MouseEvent evt) {

    }

    private JScrollPane scrollPane;
    private JTextArea descriptionLabel;
    private JLabel firstItem;
    private JLabel fourthItem;
    private JPanel panel;
    private JLabel qtyFirstItem;
    private JLabel qtySecondItem;
    private JLabel qtyThirdItem;
    private JLabel qtyFourthItem;
    private JLabel secondItem;
    private JLabel thirdItem;

    private void setImage(JLabel label, String path) throws ImageNotFoundException {
        URL imgUrl = getClass().getResource(path);
        if (imgUrl != null) {
            int dimension = 77;
            ImageIcon icon = new ImageIcon(imgUrl);
            Image image = icon.getImage();
            Image scaledImage = image.getScaledInstance(dimension, dimension, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImage));
        } else {
            throw new ImageNotFoundException("Resource not found: " + path);
        }
    }

    @Override
    public void open() {
        this.setVisible(true);
    }

    private void setDescription(int item) {
        item = item - 1;
        if (it[item] != null) {
            descriptionLabel.setText("");
            descriptionLabel.append("Nome: " + it[item].getName() + "\n");
            if (it[item] instanceof Item i) {
                if (i.getUses() != -1)
                    descriptionLabel.append("Utilizzi rimanenti: " + i.getUses() + "\n");
            }
            descriptionLabel.append("Descrizione: " + it[item].getDescription());
        }
    }

    @Override
    public void close() {
        this.dispose();
    }

    @Override
    public void linkController(Controller c) {
        // controller non necessario per questa finestra
    }
}