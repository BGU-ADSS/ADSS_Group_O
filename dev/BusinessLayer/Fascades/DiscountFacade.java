    package BusinessLayer.Fascades;

    import BusinessLayer.Objects.Discount;

    import java.time.LocalDate;
    import java.util.HashMap;


    public class DiscountFacade {
        private HashMap<Integer, Discount> productDiscounts;
        private HashMap<Integer, Discount> categoryDiscounts;
        private int prodID;
        private int categID;

        public DiscountFacade() {
            this.productDiscounts = new HashMap<>();
            this.categoryDiscounts = new HashMap<>();
            prodID = 0;
            categID = 0;
        }


        public Discount builedProductDiscount(double percent, LocalDate startDate, LocalDate endDate) throws Exception{
            if (!LocalDate.now().isBefore(startDate) && !LocalDate.now().isAfter(endDate)) {
                if(percent > 0 && percent < 100){
                Discount discount = new Discount(prodID, percent, startDate, endDate);
                productDiscounts.put(prodID, discount);
                prodID++;
                return discount;}
                throw new Exception("Percentage is invalid");
            }
            else throw new Exception("Date is invalid");
        }

        public Discount builedCategoryDiscount(double percent, LocalDate startDate, LocalDate endDate) throws Exception{

            if (!LocalDate.now().isBefore(startDate) && !LocalDate.now().isAfter(endDate)) {
                if(percent > 0 && percent < 100){
                    Discount discount = new Discount(categID, percent, startDate, endDate);
                    categoryDiscounts.put(categID, discount);
                    categID++;
                    return discount;}
                throw new Exception("Percentage is invalid");
            }
            else throw new Exception("Date is invalid");
        }

//        public Discount getActiveProductDiscount(int productId, LocalDate time)
//        {
//            Discount discount = productDiscounts.get(productId);
//            if (discount != null) {
//                if (!time.isBefore(discount.getStartDate()) && !time.isAfter(discount.getEndDate())) {
//                    return discount;
//                }
//            }
//            return null;
//        }
        public Discount getDiscountByProductID(int prodID) throws Exception{
            if(productDiscounts.get(prodID) == null){
                throw new Exception("Discount for product with given ID doesn't exist");
            }
            return productDiscounts.get(prodID);
        }
    }
