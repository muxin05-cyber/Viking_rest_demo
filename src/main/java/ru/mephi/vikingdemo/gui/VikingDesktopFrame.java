package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.controller.VikingListener;
import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.SpecificVikingService;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;


public class VikingDesktopFrame extends JFrame {

    private final VikingService vikingService;
    private final VikingTableModel tableModel = new VikingTableModel();
    private final SpecificVikingService specificVikingService;
    private final VikingListener listener;

    public VikingDesktopFrame(VikingService vikingService, VikingListener listener) {
        this.vikingService = vikingService;
        this.listener = listener;
        this.specificVikingService = new SpecificVikingService(vikingService);

        setTitle("Viking Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Viking Demo", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, 18f));
        add(header, BorderLayout.NORTH);

        JTable vikingTable = new JTable(tableModel);
        vikingTable.setRowHeight(28);
        add(new JScrollPane(vikingTable), BorderLayout.CENTER);

        JButton createButton = new JButton("Create random viking");
        createButton.addActionListener(event -> onCreateViking());
        JButton additionalButton = new JButton("Additional functional");
        additionalButton.addActionListener(event -> openAdditionalMenu());
        JButton generateButton = new JButton("Generate 10 Vikings");
        generateButton.addActionListener(event -> {
            vikingService.createRandomVikings(10);
            refreshTable();
        });


        JPanel bottomPanel = new JPanel();
        bottomPanel.add(createButton);
        bottomPanel.add(additionalButton);
        add(bottomPanel, BorderLayout.SOUTH);
        bottomPanel.add(generateButton);
        
        onInit();
    }

    private void onCreateViking() {
        Viking viking = vikingService.createRandomViking();
        tableModel.addViking(viking);
    }

    private void openAdditionalMenu(){
        VikingSpecificForm frame = new VikingSpecificForm(specificVikingService, vikingService);
        listener.setGuiMenu(frame);
        frame.setVisible(true);
    }
    
    public void addNewViking(Viking viking){
        tableModel.addViking(viking);
    }

    private void onInit() {
        List<Viking> all = vikingService.findAll();
        if (!all.isEmpty()){
            for (Viking viking : all) {
                tableModel.addViking(viking);
            }
        }
    }

    private void refreshTable() {
        List<Viking> all = vikingService.findAll();
        for (Viking viking : all) {
            tableModel.addViking(viking);
        }
    }
}
