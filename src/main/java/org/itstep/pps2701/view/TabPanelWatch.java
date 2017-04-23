package org.itstep.pps2701.view;

import org.itstep.pps2701.Utils;
import org.itstep.pps2701.dto.WatchWrapper;
import org.itstep.pps2701.enums.Watch_type;
import org.itstep.pps2701.service.ProducerService;
import org.itstep.pps2701.service.WatchService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.Date;

// Вкладка "Часы"
public class TabPanelWatch extends JPanel{
    private static JLabel lblQuantity = new JLabel("Количество");
    private static JLabel lblPrice = new JLabel("Цена");
    private static JLabel lblTrademark = new JLabel("Торговая марка");
    private static JLabel lblBoxType = new JLabel("Тип часов");
    private static JLabel lblBoxProducer = new JLabel("Производитель");

    private JTable watchTable;
    private JPanel tabPanelWatch;
    private MainFrame parentFrame;

    //@Inject
    private WatchService watchService; // действия производимые с пользователями

    private ProducerService producerService;


    public TabPanelWatch(JTabbedPane tabbedPane, MainFrame parentFrame) {
        producerService=Utils.getInjector().getInstance(ProducerService.class);
        watchService = Utils.getInjector().getInstance(WatchService.class);

        this.parentFrame = parentFrame;
        buildTabPanelWatch();
        tabbedPane.addTab("Часы", tabPanelWatch);
    }

    private void buildTabPanelWatch() {
        try {
            watchTable = new JTable(tableModelBuider(watchService.findAllActive()));
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

        tabPanelWatch = new JPanel(new BorderLayout(5, 5));
        tabPanelWatch.add(new JScrollPane(watchTable), BorderLayout.CENTER);
        tabPanelWatch.add(btnPanel, BorderLayout.SOUTH);
    }

    // создание диалога для добавления записи в бд
    private void createAddDialog() {
        JDialog addDialog = new JDialog(parentFrame, "Добавление записи", true);
        addDialog.setLocationRelativeTo(parentFrame);

        JTextField txtFieldQuantity = new JTextField(25);
        JTextField txtFieldPrice = new JTextField(25);
        JTextField txtFieldTrademark = new JTextField(25);

        txtFieldQuantity.setToolTipText(lblQuantity.getText());
        txtFieldPrice.setToolTipText(lblPrice.getText());
        txtFieldTrademark.setToolTipText(lblTrademark.getText());

        JComboBox cboxType = new JComboBox<>(Watch_type.values());
        JComboBox cboxProducer = null;
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
                WatchWrapper watchWrapper;
//                ProducerWrapper producerWrapper;
                if(!txtFieldQuantity.getText().isEmpty()
                        && !txtFieldPrice.getText().isEmpty()
                        && !txtFieldTrademark.getText().isEmpty()
                    ){
                    watchWrapper = new WatchWrapper();
//                    producerWrapper = (ProducerWrapper)cboxProducer.getSelectedItem();//
                    watchWrapper.setDateOpen(new Timestamp(System.currentTimeMillis()));
                    watchWrapper.setQuantity(Integer.parseInt(txtFieldQuantity.getText()));
                    watchWrapper.setPrice(Float.parseFloat(txtFieldPrice.getText()));
                    watchWrapper.setTrademark(txtFieldTrademark.getText());
                    watchWrapper.setType((Watch_type)cboxType.getSelectedItem());
//                    watchWrapper.setProducerWrapper(); // TODO: установка производителя
//                    watchWrapper.setUser();     // TODO: установка пользователя создавшего запись делается в сервисе

                    java.util.List<WatchWrapper> watchWrapperList = watchService.create(watchWrapper);
                    watchTable.setModel(tableModelBuider(watchWrapperList));
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
        JDialog editDialog = new JDialog(parentFrame, "Редактирование данных о производителе", true);
        editDialog.setLocationRelativeTo(parentFrame);

        JPanel editDialogPanel = new JPanel(new GridLayout(7,1));

        String id = String.valueOf(watchTable.getValueAt(selectedRow, 0));

        WatchWrapper watchWrapper = new WatchWrapper();
        watchWrapper.setId(id);
        try{
            watchWrapper = watchService.read(id);
        } catch (Exception ex){
            ex.printStackTrace();
            parentFrame.createErrorDialog(ex.getMessage());
        }

        // TODO: добавить значения обьекта в поля
        JTextField txtFieldQuantity = new JTextField(String.valueOf(watchWrapper.getQuantity()),25); // >_<
        JTextField txtFieldPrice = new JTextField(25);
        JTextField txtFieldTrademark = new JTextField(25);
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
                    WatchWrapper watchWrapper = null;
                    if(!txtFieldQuantity.getText().isEmpty()
                            && !txtFieldPrice.getText().isEmpty()
                            && !txtFieldTrademark.getText().isEmpty()){
                        watchWrapper = new WatchWrapper();
                        watchWrapper.setId(id);
                        watchWrapper.setDateOpen(new Date(System.currentTimeMillis()));
                        watchWrapper.setQuantity(Integer.parseInt(txtFieldQuantity.getText()));
                        watchWrapper.setPrice(Float.parseFloat(txtFieldPrice.getText()));
                        watchWrapper.setTrademark(txtFieldTrademark.getText());
//                        watchWrapper.setType();
//                    watchWrapper.setIdProducer(); // TODO: установка производителя
//                    watchWrapper.setUser();     // TODO: установка пользователя создавшего запись
                    } else {
                        parentFrame.createErrorDialog("Проверьте корректность ввода данных");
                    }
                    java.util.List<WatchWrapper> watchWrapperList = watchService.update(watchWrapper); // вызов метода обновления данных пользователя + перестройка данных в таблице
                    watchTable.setModel(tableModelBuider(watchWrapperList));
                    editDialog.dispose();
                }catch (Exception ex){
                    ex.printStackTrace();
                    parentFrame.createErrorDialog(ex.getMessage());
                }
            }
        });

        removeBtn.addActionListener(b -> {
            try{
                java.util.List<WatchWrapper> watchWrapperList = watchService.delete(id);
                watchTable.setModel(tableModelBuider(watchWrapperList));
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


    private DefaultTableModel tableModelBuider(java.util.List<WatchWrapper> watchWrapperList) {
        String[] tableHeader = {
                "id",
                "Количество",
                "Цена",
                "Торговая марка",
                "Тип",
                "Производитель",
                "Пользователь",
                "Дата создания",
                "Дата закрытия",
        };
        DefaultTableModel tableModel = new DefaultTableModel(tableHeader, 0);

        for(WatchWrapper watchWrapper : watchWrapperList) {
            tableModel.addRow(watchWrapper.toObject());
        }
        return tableModel;
    }
}
