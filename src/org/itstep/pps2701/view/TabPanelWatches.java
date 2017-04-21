package org.itstep.pps2701.view;

import org.itstep.pps2701.entities.Watch;
import org.itstep.pps2701.service.WatchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.sql.Timestamp;

// Вкладка "Часы"
public class TabPanelWatches extends JPanel{
    private static JLabel lblQuantity = new JLabel("Количество");
    private static JLabel lblPrice = new JLabel("Цена");
    private static JLabel lblTrademark = new JLabel("Торговая марка");
    private static JLabel lblProducer = new JLabel("Производитель");
    private static JLabel lblType = new JLabel("Тип часов");

    private JTable watchTable;
    private JPanel tabPanelWatch;
    private MainFrame parentFrame;

    private WatchService watchService = new WatchService(); // действия производимые с пользователями

    public TabPanelWatches(JTabbedPane tabbedPane, MainFrame parentFrame) {
        this.parentFrame = parentFrame;
        buildTabPanelProducers();
        tabbedPane.addTab("Часы", tabPanelWatch);
    }

    private void buildTabPanelProducers() {
        tabPanelWatch = new JPanel(new BorderLayout(5,5));

        try { watchTable = new JTable(tableBuider(watchService.read()));
        } catch (SQLException ex) { parentFrame.createErrorDialog(ex.getMessage());}

        watchTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabPanelWatch.add(new JScrollPane(watchTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // + панель управляющих кнопок вкладки "Пользователи"
        JButton addBtn = new JButton("Добавить");
        addBtn.addActionListener(b -> createAddDialog());
        btnPanel.add(addBtn);

        JButton editBtn = new JButton("Редактировать выбранную запись");
        editBtn.addActionListener(b -> createEditDialog(watchTable.getSelectedRow()));
        btnPanel.add(editBtn);

        tabPanelWatch.add(btnPanel, BorderLayout.SOUTH);
    }

    // создание диалога для добавления записи в бд
    private void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление производителя", true);
        addDialog.setLocationRelativeTo(parentFrame);

        JPanel insertDialogPanel = new JPanel();
        insertDialogPanel.setLayout(new GridLayout(5,1));

        // TODO: вынести отдельно!!! все поля ввода и их подписи
        insertDialogPanel.add(lblQuantity);
        JTextField txtFieldQuantity = new JTextField(25);
        txtFieldQuantity.setToolTipText(lblQuantity.getText());
        insertDialogPanel.add(txtFieldQuantity);

        insertDialogPanel.add(lblPrice);
        JTextField txtFieldPrice = new JTextField(25);
        txtFieldPrice.setToolTipText(lblPrice.getText());
        insertDialogPanel.add(txtFieldPrice);

        insertDialogPanel.add(lblTrademark);
        JTextField txtFieldTrademark = new JTextField(25);
        txtFieldTrademark.setToolTipText(lblTrademark.getText());
        insertDialogPanel.add(txtFieldTrademark);

        insertDialogPanel.add(lblType);
        JTextField txtFieldType = new JTextField(25);
        txtFieldType.setToolTipText(lblType.getText());
        insertDialogPanel.add(txtFieldType);

        insertDialogPanel.add(lblProducer);
        JTextField txtFieldProducer = new JTextField(25);
        txtFieldProducer.setToolTipText(lblProducer.getText());
        insertDialogPanel.add(txtFieldProducer);

        JPanel btnPanel = new JPanel();

        JButton saveBtn = new JButton("Сохранить");
        saveBtn.addActionListener( b -> {
            try{
                Watch watch = new Watch();
    // TODO:!!!!!!!!!!!!!!!!!!!
                if(!"".equals(txtFieldQuantity.getText())
                        && !"".equals(txtFieldPrice.getText())
                        && !"".equals(txtFieldTrademark.getText())
                        && !"".equals(txtFieldProducer.getText())
                    ){
                    watch.setDateOpen(new Timestamp(System.currentTimeMillis()));
                    watch.setQuantity(Integer.parseInt(txtFieldQuantity.getText()));
                    watch.setPrice(Double.parseDouble(txtFieldPrice.getText()));
                    watch.setTrademark(txtFieldTrademark.getText());
//                    watch.setProducer(); // TODO: установка производителя
//                    watch.setUser();     // TODO: установка пользователя создавшего запись

                    java.util.List<Watch> producerList = watchService.create(watch);

                    watchTable.setModel(tableBuider(producerList));
                    addDialog.dispose();
                } else {
                    parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                }
            }catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(b -> addDialog.dispose());

        btnPanel.add(saveBtn);
        btnPanel.add(cancelBtn);

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
        JDialog editDialog = new JDialog(parentFrame, "Редактирование данных о производителе", true);
        editDialog.setLocationRelativeTo(parentFrame);

        JPanel editDialogPanel = new JPanel(new GridLayout(7,1));

        int id = (Integer) watchTable.getValueAt(selectedRow, 0);

        Watch producer = new Watch();
        producer.setId(id);
        try{
            producer = watchService.getWatchById(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        // TODO: ВЫНЕСТИ ОТДЕЛЬНО ВСЕ ЭЛЕМЕНТЫ
        editDialogPanel.add(lblProducerName);
        JTextField txtFieldName = new JTextField(producer.getName(), 25);
        txtFieldName.setToolTipText(lblProducerName.getText());
        editDialogPanel.add(txtFieldName);

        editDialogPanel.add(lblProducerCountry);
        JTextField txtFieldCountry = new JTextField(producer.getCountry(), 25);
        txtFieldCountry.setToolTipText(lblProducerCountry.getText());
        editDialogPanel.add(txtFieldCountry);

        JPanel btnPanel = new JPanel();
        JButton updateBtn = new JButton("Сохранить");
        updateBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    Producer producerFin = new Producer();
                    if(!"".equals(txtFieldName.getText()) && !"".equals(txtFieldCountry.getText())) {
                        producerFin.setId(id);
                        producerFin.setName(txtFieldName.getText());
                        producerFin.setCountry(txtFieldCountry.getText());
                    } else {
                        parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                    }
                    java.util.List<Producer> producerList = producerService.update(producerFin); // вызов метода обновления данных пользователя + перестройка данных в таблице
                    producersTable.setModel(producersTableBuider(producerList));
                    editDialog.dispose();
                }catch (Exception ex){
                    ex.printStackTrace();
                    parentFrame.createErrorDialog(ex.getMessage());
                }
            }
        });

        JButton removeBtn = new JButton("Удалить");
        removeBtn.addActionListener(b -> {
            try{
                java.util.List<Watch> producerList = watchService.remove(id);
                watchTable.setModel(tableBuider(producerList));
                editDialog.dispose();
            } catch (Exception ex){
                ex.printStackTrace();
                parentFrame.createErrorDialog(ex.getMessage());
            }
        });

        JButton cancelBtn = new JButton("Отмена");
        cancelBtn.addActionListener(b -> editDialog.dispose());

        btnPanel.add(updateBtn);
        btnPanel.add(removeBtn);
        btnPanel.add(cancelBtn);

        editDialogPanel.add(btnPanel, "south");
        editDialog.add(editDialogPanel);
        editDialog.pack();
        editDialog.setVisible(true);
    }

    public DefaultTableModel tableBuider(java.util.List<Watch> watchList) {
        String[] tableHeader = {"id", "Дата создания записи", "Дата закрытия записи", "Количество", "Цена", "Торговая марка", "Тип", "Производитель", "Пользователь"};
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);

        for(Watch watch: watchList) {
            tableModel.addRow(watch.toObject());
        }
        return tableModel;
    }
}
