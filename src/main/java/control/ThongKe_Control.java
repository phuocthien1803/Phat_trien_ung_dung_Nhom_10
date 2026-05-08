package control;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import dao.ThongKe_DAO;
import static dao.ThongKe_DAO.getListDoanhThu7Day;
import static dao.ThongKe_DAO.getListDoanhThuNam;
import static dao.ThongKe_DAO.getListNhanVien;
import static dao.ThongKe_DAO.getMonAnList;
import static dao.ThongKe_DAO.getMonAnList1;
import entity.ChiTietHD_MonAn;
import entity.HoaDon;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class ThongKe_Control {

    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> yearComboBox;


    @FXML
    private BarChart<String, Number> employeeRevenueBarChart;
    @FXML
    private BarChart<String, Number> revenueBarChart;
    @FXML
    private PieChart revenuePieChart;
    @FXML
    private Label lbDoanhThuThang;
    @FXML
    private Label lbDoanhThuNam;
    @FXML
    private Label lbSoHoaDon;


    public Label revenueTodayLabel;
    public Label totalInvoicesLabel;
    public Label totalDishesSoldLabel;
    public Label newCustomersLabel;
    public PieChart dishesPieChart;
    public LineChart<String, Number> revenueLast7DaysChart;
    @FXML
    private DatePicker pickDate;


    public void updateDataPieChart(LocalDate date) {

        ObservableList<ChiTietHD_MonAn> monAnList = getMonAnList1(date);

        // Sắp xếp danh sách theo doanh thu giảm dần
        monAnList.sort((a, b) -> Double.compare(b.getThanhTien(), a.getThanhTien()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        double othersTotal = 0.0;
        int maxNameLength = 15; // Giới hạn độ dài tên món ăn

        // Thêm 5 món có doanh thu cao nhất
        for (int i = 0; i < monAnList.size(); i++) {
            ChiTietHD_MonAn item = monAnList.get(i);
            String tenMonAn = item.getTenMonAn();

            // Rút gọn tên nếu vượt quá độ dài giới hạn
            if (tenMonAn.length() > maxNameLength) {
                tenMonAn = tenMonAn.substring(0, maxNameLength - 3) + "...";
            }

            if (i < 5) {
                // Thêm món vào PieChart
                PieChart.Data data = new PieChart.Data(tenMonAn, item.getThanhTien());
                pieChartData.add(data);
            } else {
                // Gộp doanh thu các món còn lại
                othersTotal += item.getThanhTien();
            }
        }

        // Nếu có doanh thu từ "Khác", thêm vào PieChart
        if (othersTotal > 0) {
            pieChartData.add(new PieChart.Data("Khác", othersTotal));
        }

        // Gán dữ liệu vào PieChart
        dishesPieChart.setData(pieChartData);
    }
    // Method to populate BarChart with data
    public void loadDataIntoPieChart(int month, int year) {

        // Lấy dữ liệu từ cơ sở dữ liệu
        ObservableList<ChiTietHD_MonAn> monAnList = getMonAnList(month, year);

        // Sắp xếp danh sách theo thứ tự doanh thu giảm dần
        monAnList.sort((a, b) -> Double.compare(b.getThanhTien(), a.getThanhTien()));

        // Tạo danh sách dữ liệu cho PieChart
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        double othersRevenue = 0; // Tổng doanh thu của các món còn lại
        int limit = 5; // Giới hạn số món hiển thị
        int maxNameLength = 15; // Giới hạn độ dài tên món ăn

        for (int i = 0; i < monAnList.size(); i++) {
            ChiTietHD_MonAn item = monAnList.get(i);
            String tenMonAn = item.getTenMonAn();

            // Rút gọn tên nếu vượt quá độ dài giới hạn
            if (tenMonAn.length() > maxNameLength) {
                tenMonAn = tenMonAn.substring(0, maxNameLength - 3) + "...";
            }

            if (i < limit) {
                // Thêm 5 món đầu tiên vào PieChart
                pieChartData.add(new PieChart.Data(tenMonAn, item.getThanhTien()));
            } else {
                // Gộp doanh thu các món còn lại
                othersRevenue += item.getThanhTien();
            }
        }

        // Nếu có doanh thu "Khác", thêm vào PieChart
        if (othersRevenue > 0) {
            pieChartData.add(new PieChart.Data("Khác", othersRevenue));
        }

        // Gán dữ liệu cho PieChart
        revenuePieChart.setData(pieChartData);
    }
    public void loadDataIntoBarChart(int month, int year) {
        // Lấy danh sách nhân viên và doanh thu theo tháng và năm
        ObservableList<NhanVien> nhanVienList = getListNhanVien(month, year);

        // Tạo một Series dữ liệu mới cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu tháng " + month + " năm " + year);

        // Duyệt qua tất cả nhân viên và thêm vào biểu đồ
        for (NhanVien nhanVien : nhanVienList) {
            double doanhThu = 0;

            // Nếu doanh thu bằng 0, thay đổi thành 1 để hiển thị trên biểu đồ
            if (nhanVien.getDoanhThu() == 0.0) {
                doanhThu = 1;  // Gán doanh thu bằng 1 nếu doanh thu là 0
            }
            else doanhThu=nhanVien.getDoanhThu();

            // Thêm nhân viên vào biểu đồ
            XYChart.Data<String, Number> data = new XYChart.Data<>(nhanVien.getTenNV(), doanhThu);
            series.getData().add(data);
        }

        // Xóa dữ liệu cũ và thêm Series mới vào BarChart
        employeeRevenueBarChart.getData().clear();
        employeeRevenueBarChart.getData().add(series);

        // Cập nhật trục X (CategoryAxis) để hiển thị tên nhân viên
        CategoryAxis xAxis = (CategoryAxis) employeeRevenueBarChart.getXAxis();
        ObservableList<String> categories = FXCollections.observableArrayList(
                nhanVienList.stream()
                        .map(NhanVien::getTenNV)  // Lấy tên tất cả nhân viên
                        .collect(Collectors.toList())
        );
        xAxis.setAutoRanging(false);  // Đảm bảo rằng trục X không tự động thay đổi
        xAxis.setCategories(categories);

        // Cập nhật trục Y (NumberAxis) để hỗ trợ giá trị doanh thu
        NumberAxis yAxis = (NumberAxis) employeeRevenueBarChart.getYAxis();
        yAxis.setAutoRanging(true);  // Cho phép trục Y tự điều chỉnh để hiển thị giá trị doanh thu đúng

        // Đảm bảo rằng trục Y bắt đầu từ 0 nếu có giá trị doanh thu bằng 0
        if (yAxis.getLowerBound() > 0) {
            yAxis.setLowerBound(0);  // Đặt giá trị thấp nhất của trục Y là 0
        }
    }
    public void loadDataRevenueBarChart(int year) {
        ObservableList<HoaDon> hoaDonList = getListDoanhThuNam(year);

        Map<String, Double> revenueByMonth = new LinkedHashMap<>();
        for (int i = 1; i <= 12; i++) {
            revenueByMonth.put("Tháng " + i, 0.0);
        }

        // Tính tổng doanh thu cho từng tháng
        for (HoaDon hoaDon : hoaDonList) {
            int monthValue = hoaDon.getNgayDat().getMonthValue(); // Lấy tháng (giá trị số từ 1 đến 12)
            String monthKey = "Tháng " + monthValue; // Tạo khóa "Tháng x"
            revenueByMonth.put(monthKey, revenueByMonth.get(monthKey) + hoaDon.getTongTien());
        }

        // Tạo Series dữ liệu cho BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu năm " + year);

        // Thêm dữ liệu doanh thu vào Series
        for (Map.Entry<String, Double> entry : revenueByMonth.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Xóa dữ liệu cũ và thêm Series mới vào BarChart
        revenueBarChart.getData().clear();
        revenueBarChart.getData().add(series);
    }
    private void populateRevenueLast7DaysChart(LineChart<String, Number> chart,LocalDate day) {
        ObservableList<HoaDon> list = getListDoanhThu7Day(day);

        // Sắp xếp dữ liệu từ ngày xa nhất đến ngày gần nhất
        FXCollections.sort(list, (a, b) -> a.   getNgayDat().compareTo(b.   getNgayDat()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu 7 ngày");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");

        for (HoaDon hoaDon : list) {
            String date = hoaDon.getNgayDat().format(formatter);
            series.getData().add(new XYChart.Data<>(date, hoaDon.getTongTien()));
        }


        chart.getData().clear();
        chart.getData().add(series);
    }





    public void initialize() {
        pickDate.setValue(LocalDate.now());
        pickDate.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleEnterKey();
                event.consume();
            }
        });
    }
    public void handleClick( ) {
        //Tab tổng quan
        int selectedYear = Integer.parseInt(yearComboBox.getValue());
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        loadDataRevenueBarChart(selectedYear);
        lbDoanhThuNam.setText(decimalFormat.format(ThongKe_DAO.getDoanhThuNam(selectedYear)));
        if (monthComboBox.getValue()!=null){
            Integer selectedMonth = Integer.parseInt(monthComboBox.getValue());
            loadDataIntoPieChart(selectedMonth,selectedYear);
            loadDataIntoBarChart(selectedMonth,selectedYear);
            lbDoanhThuThang.setText(decimalFormat.format(ThongKe_DAO.getDoanhThuThang(selectedMonth,selectedYear)));
            lbSoHoaDon.setText(String.valueOf(ThongKe_DAO.getHoaDonThang(selectedMonth,selectedYear)));
        }
    }

    private void handleEnterKey() {
        LocalDate date = pickDate.getValue();
        DecimalFormat decimalFormat= new DecimalFormat("#,###.##");
        populateRevenueLast7DaysChart(revenueLast7DaysChart,date);
        updateDataPieChart(date);
        revenueTodayLabel.setText(decimalFormat.format(ThongKe_DAO.getDoanhThuDay(date)));
        totalInvoicesLabel.setText(String.valueOf(ThongKe_DAO.getHoaDonDay(date)));
        totalDishesSoldLabel.setText(String.valueOf(ThongKe_DAO.getMonAnCountDay(date)));
        newCustomersLabel.setText(String.valueOf(ThongKe_DAO.getNewCustomerCountDay(date)));
    }

    public void handleClick1() {
        //Tab cụ thể
        DecimalFormat decimalFormat= new DecimalFormat("#,###.##");
        LocalDate date = pickDate.getValue();
        populateRevenueLast7DaysChart(revenueLast7DaysChart,date);
        updateDataPieChart(date);
        revenueTodayLabel.setText(decimalFormat.format(ThongKe_DAO.getDoanhThuDay(date)));
        totalInvoicesLabel.setText(String.valueOf(ThongKe_DAO.getHoaDonDay(date)));
        totalDishesSoldLabel.setText(String.valueOf(ThongKe_DAO.getMonAnCountDay(date)));
        newCustomersLabel.setText(String.valueOf(ThongKe_DAO.getNewCustomerCountDay(date)));
    }
}

