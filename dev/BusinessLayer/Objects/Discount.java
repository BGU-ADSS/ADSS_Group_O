package BusinessLayer.Objects;


import java.time.LocalDate;
import java.time.LocalTime;

public class Discount {
        private int discountID;
        private double percent;
        private LocalDate startDate;
        private LocalDate endDate;

        public Discount(int discountID, double percent, LocalDate startDate, LocalDate endDate) {
            this.discountID = discountID;
            this.percent = percent;
            this.startDate = startDate;
            this.endDate = endDate;
        }
        public int getDiscountID() {
            return discountID;
        }
        public double getPercent() {
            return percent;
        }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() {
            return endDate;
        }

}
