/**
* Bond is a child class of FinancialAsset
*/
public class Bond extends FinancialAsset{
  private double faceValue;
  private double couponRate;
  private int yearsToMaturity;

  public Bond(String name, double currentValue, int riskRating,
              double faceValue, double couponRate, int yearsToMaturity){
    super(name, currentValue, riskRating);
    this.faceValue = faceValue;
    this.couponRate = couponRate;
    this.yearsToMaturity = yearsToMaturity;
  }

  public double getFaceValue(){
    return faceValue;
  }

  public void setFaceValue(double faceValue){
    this.faceValue = faceValue;
  }

  public double getCouponRate(){
    return couponRate;
  }

  public void setCouponRate(double couponRate){
    this.couponRate = couponRate;
  }

  public int getYearsToMaturity(){
    return yearsToMaturity;
  }

  public void setYearsToMaturity(int yearsToMaturity){
    this.yearsToMaturity = yearsToMaturity;
  }

  @Override
  public double calculateReturn(){
    return faceValue * couponRate;
  }

  @Override
  public void displayInfo(){
    System.out.println("--- Bond ---");
    super.displayInfo();
    System.out.printf("Face Value:      $%.2f%n", faceValue);
    System.out.printf("Coupon Rate:     %.2f%%%n", couponRate * 100);
    System.out.println("Years To Maturity: " + yearsToMaturity);
    System.out.printf("Expected Return: $%.2f%n", calculateReturn());
    System.out.println();
  }
}
