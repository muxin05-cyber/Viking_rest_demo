package ru.mephi.vikingdemo.gui;

import ru.mephi.vikingdemo.model.Viking;
import ru.mephi.vikingdemo.service.SpecificVikingService;
import ru.mephi.vikingdemo.service.VikingService;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VikingSpecificForm extends JFrame {
    private final SpecificVikingService specificVikingService;
    private final JTextArea textArea;
    private final VikingService vikingService;

    public VikingSpecificForm(SpecificVikingService specificVikingService, VikingService vikingService) {
        this.specificVikingService = specificVikingService;
        this.vikingService = vikingService;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(new Dimension(1000, 420));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        setTitle("Additional functional");

        this.textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton button1 = new JButton("Random Viking (>180 cm)");
        button1.addActionListener(event -> showRandomTallViking());
        bottomPanel.add(button1);

        JButton button2 = new JButton("Legendary Equipment");
        button2.addActionListener(event -> showVikingsWithLegendaryEquipment());
        bottomPanel.add(button2);

        JButton button3 = new JButton("Red-Bearded by Age");
        button3.addActionListener(event -> showRedBeardedSortedByAge());
        bottomPanel.add(button3);

        JButton button4 = new JButton("Max ID");
        button4.addActionListener(event -> showMaxId());
        bottomPanel.add(button4);

        JButton button5 = new JButton("Even IDs");
        button5.addActionListener(event -> showEvenIds());
        bottomPanel.add(button5);



        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private void showRandomTallViking() {
        String descriptionRandomViking = specificVikingService.getRandomVikingWithHeightMore180();
        if (descriptionRandomViking.isEmpty()){
            textArea.setText("Vikings with height more than 180 did not find");
            return;
        }
        textArea.setText(descriptionRandomViking);

    }

    private void showVikingsWithLegendaryEquipment() {
        List<Viking> vikings = specificVikingService.getVikingsWithLegendaryEquipment();

        if (vikings.isEmpty()) {
            textArea.setText("Vikings with fabulous equipment did not find");
            return;
        }


        textArea.setText(stringWithVikings("Vikings with fabulous equipment\n\n", vikings));
    }

    private void showRedBeardedSortedByAge() {
        List<Viking> vikings = specificVikingService.getRedBeardedSortedByAge();
        if (vikings.isEmpty()) {
            textArea.setText("Vikings with red beard did not find");
            return;
        }
        textArea.setText(stringWithVikings("Vikings with red beard (sorted by age)\n\n", vikings));
    }

    private void showMaxId() {
        textArea.setText(specificVikingService.getMaxIdFormatted());
    }

    private void showEvenIds() {
        textArea.setText(specificVikingService.getEvenIdsFormatted());
    }

    private String stringWithVikings(String title, List <Viking> vikings){
        StringBuilder sb = new StringBuilder();
        sb.append(title);

        for (int i = 0; i < vikings.size(); i++) {
            Viking v = vikings.get(i);
            sb.append(String.format("%d. %s\n", i + 1, v.name()));
            sb.append(String.format("   Age: %d | Height: %d cm\n", v.age(), v.heightCm()));
            sb.append(String.format("   Hair: %s | Beard: %s\n", v.hairColor(), v.beardStyle()));
            sb.append(String.format("   Equipment: %s\n\n",
                    v.equipment().stream()
                            .map(e -> e.name() + " [" + e.quality() + "]")
                            .collect(Collectors.joining(", "))));
        }
        return sb.toString();
    }
}