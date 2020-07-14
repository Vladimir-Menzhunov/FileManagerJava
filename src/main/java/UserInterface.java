import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


class UserInterface extends JFrame { //JFrame это всё поле что мы сможм задействовать, всё то поле в которое мы будем добвлять элементы
    private JList<File> filesList = new JList<File>();// Текстовый список
    private ArrayList<String> cashFolder = new ArrayList<String>(); //Арей лист для хранения пути
    private JLabel cashWait = new JLabel(".\\");


    UserInterface() throws IOException{

        super("Проводник");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//если нажать на крестик то приложение будет выключено
        setResizable(true); //Разрешаем менять ширину окна
        // Наша панель
        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BorderLayout(5,5));//отступы от нашего окна
        cPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));//Растояние между интерфейсами внутри этой панели
        // панель для кнопок
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,4,5,5)); // отступы кнопочного окна 1 строка 4 строчки отступ 5 сверху снизу
         // Создание всплывающего окна 1)К чему относится, как будет называтся, можно ли закрыть
       //панель которая будет добавлятся в наш диалог
       // Добавили панель в диалог
        //скрол пане пролистывает список если он выходит за рамки
        JScrollPane scrollPane = new JScrollPane(filesList);

        final File[] paths;// создание массива из файлов
        // возвращает пути к файлам и каталогам
        paths = File.listRoots();
        filesList.setListData(paths);//Это делает так что бы массив Discs был виден в панели скрол
        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);//разрешаем выбор в главном листе, нескольких объектов

        //добавляем слушателя мыши
        filesList.addMouseListener(new MouseListener() { //параметры для мыши
            @Override
            public void mouseClicked(MouseEvent e) {
                String val = new String(filesList.getSelectedValuesList().toString());
                String selectedObject = val.substring(1,val.length() - 1);//Сюда добавляем выделенный нами объект,т.е щелкнули по чему либо
                if (e.getClickCount() == 2) {
                    DefaultListModel model = new DefaultListModel(); // Модель в которую динамически можно добавлять по элементу
                    String fullPath = toFullPath(cashFolder);//создание переменной которая возвращает полный путь к файлу
                    File selectedFile;//то куда мы перешли
                    if (cashFolder.size() > 1) {
                        selectedFile = new File(fullPath,selectedObject + '\\');//путь от объекта к файлу
                    } else {
                        selectedFile = new File(fullPath  + selectedObject + '\\');//начинаем складывать например от начала диска в другую дирректорию
                    }

                    if (selectedFile.isDirectory()) { // является ли директорией
                        String[] rootSrt = selectedFile.list();//Закидываем в массиив стрингов всё то что находится по каталогу
                        assert rootSrt != null; //Увеличивает уверенность в том что программа не содержит ошибок
                        // Предположение исхода, если будет иначе выбросится исключение
                        for (String str : rootSrt) {
                            File checkObject = new File(selectedFile.getPath(), str);//Создаём файл для проверки , в него мы добавляем то куда мы заходим и тот элемент который там находится
                            if (!checkObject.isHidden() && !str.equals("Documents and Settings")) { // isHidden - метод показывающий виимость объекта
                                model.addElement(str);// добавляем в модель папку(элемент)
                            }
                        }
                        cashFolder.add(selectedObject + '\\');//Добавляем новый каталог в который мы рашли в кеш
                        cashWait.setText(selectedFile.getAbsolutePath());
                        filesList.setModel(model);//добавляем на лист отобранные элементы куда мы зашли

                    } else {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(selectedFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            }
            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        JButton comeBackFolder = new JButton("Назад");
        comeBackFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cashFolder.size() > 1) { // проверяем находимся ли мы на директории выше чем 0
                    DefaultListModel comebackRootModel = new DefaultListModel();// Модель в которую динамически можно добавлять по элементу
                    cashFolder.remove(cashFolder.size() - 1);//возврашаем назад на 1 меньше
                    String backDir = toFullPath(cashFolder);// склейка всего воидино
                    File comeBack = new File(toFullPath(cashFolder));
                    String[] object = new File(backDir).list();//Открываем весь список каталога в каторый заходим
                    assert object != null;
                    for (String str : object) {
                        File checkObject = new File(backDir, str);//Создаём файл для проверки , в него мы добавляем то куда мы заходим и тот элемент который там находится
                        if(!checkObject.isHidden() && !str.equals("Documents and Settings")){ // isHidden - метод показывающий виимость объекта
                            comebackRootModel.addElement(str);// добавляем в модель папку(элемент)
                        }
                    }
                    cashWait.setText(comeBack.getAbsolutePath());
                    filesList.setModel(comebackRootModel);
                } else {
                    cashFolder.removeAll(cashFolder);
                    cashWait.setText(".\\");
                    filesList.setListData(paths);
                }
            }
        });
    //добавляем слушателя который ловит только нажатие

        JButton creatingFolder = new JButton("Создать папку");//Кнопка создания папки

        creatingFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(!cashFolder.isEmpty()){ //Проверяем находимся мы в какой либо дирректори
                    String currentLocation;// текущий путь
                    File newFolder; // новая папка и путь к ней
                    CreateNewFolderJDialog newFolderJDialog = new CreateNewFolderJDialog(UserInterface.this);// Создаём модель ислодьзующую наш интерфейс

                    // Проверяем готовность
                    if(newFolderJDialog.getReady()){
                        currentLocation = toFullPath(cashFolder);// добавляем текущий путь к папке
                        newFolder = new File(currentLocation, newFolderJDialog.getNewName());// добавляем к новой паке путь
                        if(!newFolder.exists()) newFolder.mkdir();// если фаайл не существует создать его

                        File updateList = new File(currentLocation); //Обновление списка, получение существующего пути
                        String[] updateMas = updateList.list();// получение всех файло по донному пути
                        DefaultListModel updateModel = new DefaultListModel(); //создание динамической модели которая будет хранить все наши стринги
                        assert updateMas != null;// убеждаемся в том что, updateMas != 0;
                        // запускаем цикл for-each
                        for(String str : updateMas) {
                                File check = new File(updateList.getPath(),str); //склеиваем пути исходного положения файла с кождой дирректорией
                                if(!check.isHidden() && !str.equals("Documents and Settings")){
                                    updateModel.addElement(str); //добавляем элемент в модель
                                }
                            }
                        cashWait.setText(updateList.getAbsolutePath());
                        filesList.setModel(updateModel);//добавляем на лист отобранные элементы куда мы зашли

                    }
                }
            }
        });
        JButton creatFile = new JButton("Создать файл");

       creatFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!cashFolder.isEmpty()){ //Проверяем находимся мы в какой либо дирректори
                    String currentLocation;// текущий путь
                    File newFolder; // новая папка и путь к ней
                    CreateNewFileJDialog newFolderJDialog = new CreateNewFileJDialog(UserInterface.this);// Создаём модель ислодьзующую наш интерфейс

                    // Проверяем готовность
                    if(newFolderJDialog.getReady()){
                        currentLocation = toFullPath(cashFolder);// добавляем текущий путь к папке
                        newFolder = new File(currentLocation, newFolderJDialog.getNewName());// добавляем к новой паке путь
                        if(!newFolder.exists()) {
                            try {
                                newFolder.createNewFile();// если фаайл не существует создать его
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }

                        File updateList = new File(currentLocation); //Обновление списка, получение существующего пути
                        String[] updateMas = updateList.list();// получение всех файло по донному пути
                        DefaultListModel updateModel = new DefaultListModel(); //создание динамической модели которая будет хранить все наши стринги
                        assert updateMas != null;// убеждаемся в том что, updateMas != 0;
                        // запускаем цикл for-each
                        for(String str : updateMas) {
                            File check = new File(updateList.getPath(),str); //склеиваем пути исходного положения файла с кождой дирректорией
                            if(!check.isHidden() && !str.equals("Documents and Settings")){
                                updateModel.addElement(str); //добавляем элемент в модель
                            }
                        }
                        cashWait.setText(updateList.getAbsolutePath());
                        filesList.setModel(updateModel);//добавляем на лист отобранные элементы куда мы зашли

                    }
                }
            }

        });

        JButton deleteFolder = new JButton("Удалить");
        deleteFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String val = new String(filesList.getSelectedValuesList().toString());
                String selectedObject = val.substring(1,val.length() - 1);//Сюда добавляем выделенный нами объект,т.е щелкнули по чему либо
                String currentPath = toFullPath(cashFolder);//текущее местоположение
                if(!selectedObject.isEmpty()){
                    //рекурсивное удаление
                    deleteDir(new File(currentPath, selectedObject));// полный путь

                    File updateDir = new File(currentPath);
                    String updateMas[] = updateDir.list();
                    DefaultListModel updateModel = new DefaultListModel();

                    for(String str : updateMas) {
                        File check = new File(updateDir.getPath(), str);
                        if(!check.isHidden() && !str.equals("Documents and Settings")){
                            updateModel.addElement(str);
                        }
                    }
                    filesList.setModel(updateModel);
                }
            }
        });
        JButton renameFolder = new JButton("Переименовать");
        renameFolder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String val = new String(filesList.getSelectedValuesList().toString());
                String selectedObject = val.substring(1,val.length() - 1);//Сюда добавляем выделенный нами объект,т.е щелкнули по чему либо

                if(!cashFolder.isEmpty() && new File(selectedObject) != null ){

                    String currentPath = toFullPath(cashFolder);//Текущее место положение
                    RenameJDialog rename = new RenameJDialog(UserInterface.this);//создаём класскоторый работает в нашим frame
                    if(rename.getReady()){ //если объект готов
                        File renameFile = new File(currentPath, selectedObject + "\\"); //склеиваем текущее место папки с нашим объектом
                        renameFile.renameTo(new File(currentPath, rename.getNewName() + "\\"));// склеиваем место нахождения с текущим файлом

                        File updateDir = new File(currentPath);//создаём файл с директорией в которой находимся
                        String updateMas[]= updateDir.list();//добавляем в массив стрингов всё содержимое каталога
                        DefaultListModel updateModel = new DefaultListModel();//создаём динамическую модель
                        for(String str : updateMas){
                            File check = new File(updateDir.getPath(),str);//создайм файл который перебирает местоположения каждой папки
                            if(!check.isHidden() && !str.equals("Documents and Settings")){
                                updateModel.addElement(str);//lj,fdkztv d vjltkm
                            }
                        }
                        filesList.setModel(updateModel);// иницеальзируем файловый лист на фрейме
                    }
                }
            }
        });




        buttonPanel.add(comeBackFolder);
        //создание кнопки, которая создаёт папку
        buttonPanel.add(creatingFolder);
        buttonPanel.add(creatFile);
        buttonPanel.add(deleteFolder);
        buttonPanel.add(renameFolder);

        cPanel.setLayout(new BorderLayout());
        cPanel.add(scrollPane, BorderLayout.CENTER);// по центру
        cPanel.add(buttonPanel, BorderLayout.SOUTH);//После последней скроки
        cPanel.add(cashWait, BorderLayout.PAGE_START);
        getContentPane().add(cPanel);//получить панель контента


        setSize(800, 500); //желаемый размер
        setLocationRelativeTo(null);//показ по середине
        setVisible(true);// видимость
    }
    private String toFullPath(ArrayList<String> file){//метод который склеивает путь
        StringBuilder listPart = new StringBuilder();
        for(String str : file){
            listPart.append(str);
        }
        return listPart.toString();
    }
// рекурсивное удаление
    private void deleteDir(File file) {
        File[] objects = file.listFiles();// лист файлов и папок
        if(objects != null) {
            for(File f : objects){
                deleteDir(f);
            }
        }
        file.delete();
    }
}
