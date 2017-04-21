package org.itstep.pps2701.view;

import org.itstep.pps2701.entities.Producer;
import org.itstep.pps2701.entities.Watch;
import org.itstep.pps2701.enums.Watch_type;
import org.itstep.pps2701.service.ProducerService;
import org.itstep.pps2701.service.WatchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Timestamp;

// Вкладка "Часы"
public class TabPanelWatch extends JPanel{
    private static JLabel lblQuantity = new JLabel("Количество");
    private static JLabel lblPrice = new JLabel("Цена");
    private static JLabel lblTrademark = new JLabel("Торговая марка");
    private static JLabel lblBoxType = new JLabel("Тип часов");
    private static JLabel lblBoxProducer = new JLabel("Производитель");

    private static JTextField txtFieldQuantity;
    private static JTextField txtFieldPrice;
    private static JTextField txtFieldTrademark;
    private static JComboBox cboxType;
    private static JComboBox cboxProducer;

    private JTable watchTable;
    private JPanel tabPanelWatch;
    private MainFrame parentFrame;

    private WatchService watchService = new WatchService(); // действия производимые с пользователями
    private ProducerService producerService = new ProducerService();


    public TabPanelWatch(JTabbedPane tabbedPane, MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        buildTabPanelWatch();
        tabbedPane.addTab("Часы", tabPanelWatch);
    }

    private void buildTabPanelWatch() {
        tabPanelWatch = new JPanel(new BorderLayout(5, 5));

        try {
            watchTable = new JTable(tableBuilder(watchService.read()));
            watchTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        } catch (Exception ex) {
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        JButton addBtn = new JButton("Добавить");
        JButton editBtn = new JButton("Редактировать выбранную запись");

        addBtn.addActionListener(b -> createAddDialog());
        editBtn.addActionListener(b -> createEditDialog(watchTable.getSelectedRow()));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);

        tabPanelWatch.add(new JScrollPane(watchTable), BorderLayout.CENTER);
        tabPanelWatch.add(btnPanel, BorderLayout.SOUTH);
    }

    // создание диалога для добавления записи в бд
    private void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление записи", true);
        addDialog.setLocationRelativeTo(parentFrame);

        txtFieldQuantity = new JTextField(25);
        txtFieldPrice = new JTextField(25);
        txtFieldTrademark = new JTextField(25);

        txtFieldQuantity.setToolTipText(lblQuantity.getText());
        txtFieldPrice.setToolTipText(lblPrice.getText());
        txtFieldTrademark.setToolTipText(lblTrademark.getText());

        cboxType = new JComboBox<>(Watch_type.values());
        try{
            java.util.List<Object> producerNamesList = producerService.getProducerNames(); // получение списка производителей с ид и именем для вывода
            cboxProducer = new JComboBox<>(producerNamesList.toArray()); // Выбор производителя
        }catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        cboxType.setSize(25,5);
        cboxProducer.setSize(25,5);

        JButton saveBtn = new JButton("Сохранить");
        JButton cancelBtn = new JButton("Отмена");

        saveBtn.addActionListener( b -> {
            try{
                Watch watch = null;
                if(!txtFieldQuantity.getText().isEmpty()
                        && !txtFieldPrice.getText().isEmpty()
                        && !txtFieldTrademark.getText().isEmpty()
                    ){
                    watch = new Watch();
                    watch.setDateOpen(new Timestamp(System.currentTimeMillis()));
                    watch.setQuantity(Integer.parseInt(txtFieldQuantity.getText()));
                    watch.setPrice(Double.parseDouble(txtFieldPrice.getText()));
                    watch.setTrademark(txtFieldTrademark.getText());
                    watch.setType((Watch_type)cboxType.getSelectedItem());
                    watch.setIdProducer(((Producer)cboxProducer.getSelectedItem()).getId()); // TODO: установка производителя
//                    watch.setUser();     // TODO: установка пользователя создавшего запись

                    java.util.List<Watch> watchList = watchService.create(watch);
                    watchTable.setModel(tableBuilder(watchList));
                    addDialog.dispose();
                } else {
                    parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(b -> addDialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"
        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

        JPanel insertDialogPanel = new JPanel(new GridLayout(11,1));
        insertDialogPanel.add(lblQuantity);
        insertDialogPanel.add(txtFieldQuantity);
        insertDialogPanel.add(lblPrice);
        insertDialogPanel.add(txtFieldPrice);
        insertDialogPanel.add(lblTrademark);
        insertDialogPanel.add(txtFieldTrademark);
        insertDialogPanel.add(lblBoxType);
        insertDialogPanel.add(cboxType);
        insertDialogPanel.add(lblBoxProducer);
        insertDialogPanel.add(cboxProducer);
        insertDialogPanel.add(btnPanel, "south");

        addDialog.add(insertDialogPanel);
        addDialog.pack();
        addDialog.setVisible(true);
    }

    /**
     * создание диалогового окна редактирования записи производителя
     * @param selectedRow номер выбранного ряда таблицы
     */
    private void createEditDialog(int selectedRow) {
        JDialog editDialog = new JDialog(JOptionPane.getRootFrame(), "Редактирование данных о производителе", true);
        editDialog.setLocationRelativeTo(JOptionPane.getRootFrame());

        JPanel editDialogPanel = new JPanel(new GridLayout(7,1));

        int id = (Integer) watchTable.getValueAt(selectedRow, 0);

        Watch watch = new Watch();
        watch.setId(id);
        try{
            watch = watchService.getWatchById(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        // TODO: добавить значения обьекта в поля
        txtFieldQuantity = new JTextField(String.valueOf(watch.getQuantity()),25); // >_<
        txtFieldPrice = new JTextField(25);
        txtFieldTrademark = new JTextField(25);
        // TODO: cboxType
        // TODO: cboxProducer
//        txtFieldType = new JTextField(25);
//        txtFieldProducer = new JTextField(25);

        txtFieldQuantity.setToolTipText(lblQuantity.getText());
        txtFieldPrice.setToolTipText(lblPrice.getText());
        txtFieldTrademark.setToolTipText(lblTrademark.getText());
//        txtFieldType.setToolTipText(lblType.getText());
//        txtFieldProducer.setToolTipText(lblProducer.getText());

        editDialogPanel.add(lblQuantity);
        editDialogPanel.add(txtFieldQuantity);
        editDialogPanel.add(lblPrice);
        editDialogPanel.add(txtFieldPrice);
        editDialogPanel.add(lblTrademark);
        editDialogPanel.add(txtFieldTrademark);
//        editDialogPanel.add(lblType);
//        editDialogPanel.add(txtFieldType);
//        editDialogPanel.add(lblProducer);
//        editDialogPanel.add(txtFieldProducer);

        JButton saveBtn = new JButton("Сохранить");
        JButton removeBtn = new JButton("Удалить");
        JButton cancelBtn = new JButton("Отмена");

        saveBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Watch watch = null;
                    if(!txtFieldQuantity.getText().isEmpty()
                            && !txtFieldPrice.getText().isEmpty()
                            && !txtFieldTrademark.getText().isEmpty()){
                        watch = new Watch();
                        watch.setId(id);
                        watch.setDateOpen(new Timestamp(System.currentTimeMillis()));
                        watch.setQuantity(Integer.parseInt(txtFieldQuantity.getText()));
                        watch.setPrice(Double.parseDouble(txtFieldPrice.getText()));
                        watch.setTrademark(txtFieldTrademark.getText());
//                        watch.setType();
//                    watch.setIdProducer(); // TODO: установка производителя
//                    watch.setUser();     // TODO: установка пользователя создавшего запись
                    } else {
                        parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                    }
                    java.util.List<Watch> watchList = watchService.update(watch); // вызов метода обновления данных пользователя + перестройка данных в таблице
                    watchTable.setModel(tableBuilder(watchList));
                    editDialog.dispose();
                }catch (Exception ex){
                    ex.printStackTrace();
                    parentFrame.createErrorDialog(ex.getMessage());
                }
            }
        });

        removeBtn.addActionListener(b -> {
            try{
                java.util.List<Watch> watchList = watchService.remove(id);
                watchTable.setModel(tableBuilder(watchList));
                editDialog.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        cancelBtn.addActionListener(b -> editDialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"        btnPanel.add(saveBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(cancelBtn);

        editDialog.add(editDialogPanel);
        editDialogPanel.add(btnPanel, "south");
        editDialog.pack();
        editDialog.setVisible(true);
    }


    private DefaultTableModel tableBuilder(java.util.List<Watch> watchList) {
        String[] tableHeader = {"id", "Дата создания записи", "Дата закрытия записи", "Количество", "Цена", "Торговая марка", "Тип", "Производитель", "Пользователь"};
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);

        for(Watch watch: watchList) {
            tableModel.addRow(watch.toObject());
        }
        return tableModel;
    }
}
