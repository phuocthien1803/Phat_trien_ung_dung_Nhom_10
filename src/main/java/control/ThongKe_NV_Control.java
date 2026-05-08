package control;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import dao.ThongKe_NV_DAO;
import static dao.ThongKe_NV_DAO.getListDoanhThu7Day;
import static dao.ThongKe_NV_DAO.getMonAnList1;
import entity.ChiTietHD_MonAn;
import entity.HoaDon;
import entity.NhanVien;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class ThongKe_NV_Control {
    public Label revenueTodayLabel;
    private NhanVien NV;
    public Label totalInvoicesLabel;
    public Label totalDishesSoldLabel;
    public Label newCustomersLabel;
    public PieChart dishesPieChart;
    public LineChart<String, Number> revenueLast7DaysChart;



    private void populateRevenueLast7DaysChart(LineChart<String, Number> chart) {
        NV = SessionManager.getInstance().getCurrentNhanVien();
        String ma = NV.getMaNV();
        System.out.println(ma);
        ObservableList<HoaDon> list = getListDoanhThu7Day(ma);

        // Sắp xếp dữ liệu từ ngày xa nhất đến ngày gần nhất
        FXCollections.sort(list, (a, b) -> a.   getNgayDat().compareTo(b.   getNgayDat()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu 7 ngày");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM");

        for (HoaDon hoaDon : list) {
            String date = hoaDon.getNgayDat().format(formatter);
            series.getData().add(new XYChart.Data<>(date, hoaDon.getTongTien()));
        }

        // Xóa dữ liệu cũ và thêm dữ liệu mới vào biểu đồ

        chart.getData().clear();
        chart.getData().add(series);
    }



    public void updateDataPieChart(LocalDate date) {
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();
        NV = SessionManager.getInstance().getCurrentNhanVien();
        String ma = NV.getMaNV();
        System.out.println(ma);
        ObservableList<ChiTietHD_MonAn> monAnList = getMonAnList1(day, month, year,ma);

        // Sắp xếp danh sách theo 'ThanhTien' giảm dần
        monAnList.sort((a, b) -> Double.compare(b.getThanhTien(), a.getThanhTien()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        double othersTotal = 0.0;
        int maxNameLength = 15; // Giới hạn độ dài tên món ăn

        // Thêm 5 món doanh thu cao nhất vào biểu đồ
        for (int i = 0; i < monAnList.size(); i++) {
            ChiTietHD_MonAn item = monAnList.get(i);
            String tenMonAn = item.getTenMonAn();

            // Rút gọn tên nếu dài hơn giới hạn
            if (tenMonAn.length() > maxNameLength) {
                tenMonAn = tenMonAn.substring(0, maxNameLength - 3) + "...";
            }

            if (i < 5) {
                // Thêm món ăn vào PieChart
                PieChart.Data data = new PieChart.Data(tenMonAn, item.getThanhTien());
                pieChartData.add(data);
            } else {
                // Tính tổng doanh thu các món còn lại
                othersTotal += item.getThanhTien();
            }
        }

        // Thêm mục "Khác" nếu có doanh thu từ các món khác
        if (othersTotal > 0) {
            pieChartData.add(new PieChart.Data("Khác", othersTotal));
        }

        // Gán dữ liệu vào PieChart
        dishesPieChart.setData(pieChartData);
    }




    public void initialize() {
        NV = SessionManager.getInstance().getCurrentNhanVien();
        String ma = NV.getMaNV();
        LocalDate date = LocalDate.now();
        updateDataPieChart(date);
        populateRevenueLast7DaysChart(revenueLast7DaysChart);
        DecimalFormat decimalFormat= new DecimalFormat("#,###.##");
        revenueTodayLabel.setText(decimalFormat.format(ThongKe_NV_DAO.getDoanhThuToday(ma)));
        totalInvoicesLabel.setText(String.valueOf(ThongKe_NV_DAO.getHoaDonToday(ma)));
        totalDishesSoldLabel.setText(String.valueOf(ThongKe_NV_DAO.getMonAnCountToday(ma)));
        newCustomersLabel.setText(String.valueOf(ThongKe_NV_DAO.getNewCustomerCountToday(ma)));
    }
}
