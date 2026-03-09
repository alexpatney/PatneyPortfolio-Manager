import java.util.ArrayList;
import java.util.Scanner;

public class PortfolioManager{
  private static ArrayList<FinancialAsset> portfolioAssets = new ArrayList<FinancialAsset>();
  private static Scanner kb = new Scanner(System.in);

  public static void main(String[] args){
    System.out.println("==========================================");
    System.out.println("   Welcome to PatneyPortfolio Manager");
    System.out.println("==========================================");

    boolean running = true;

    while(running){
      System.out.println("---------- Main Menu ----------");
      System.out.println("1. Add Asset");
      System.out.println("2. View Portfolio");
      System.out.println("3. Sort Portfolio");
      System.out.println("4. Search Asset");
      System.out.println("5. Calculate Portfolio Summary");
      System.out.println("6. Remove Asset");
      System.out.println("7. Exit");

      int choice = getValidInt("Enter your choice", 1, 7);

      switch(choice){
        case 1:
          addAsset();
          break;
        case 2:
          viewPortfolio();
          break;
        case 3:
          sortPortfolio();
          break;
        case 4:
          searchAsset();
        case 5:
          calculateSummary();
          break;
        case 6:
          removeAsset();
          break;
        case 7:
          running = false;
          System.out.println("Thank you for using PatneyPortfolio Manager. Goodbye!");
          break;
      }
      System.out.println();
    }

    kb.close();
  }

  public static void addAsset(){}

  public static void viewPortfolio(){
    System.out.println("\n--- Your Portfolio ---");

    if(portfolioAssets.isEmpty()){
      System.out.println("Your portfolio is empty. Add some assets first!");
      return;
    }

    for(int i = 0; o < portfolioAssets.size(); i++){
      System.out.println("Asset #" + (i + 1) + ": ");
      portfolioAssets.get(i).displayInfo();
    }
  }

  public static void sortPortfolio(){}

  public static void searchAsset(){
    System.out.println("\n--- Search Asset ---");

    if(portfolioAssets.isEmpty()){
      System.out.println("Your portfolio is empty. Nothing to search.");
      return;
    }

    System.out.print("Enter asset name to search for: ");
    String searchName = kb.nextLine().trim();

    while(searchName.isEmpty()){
      System.out.print("Search term cannot be empty. Enter asset name: ");
      searchName = kb.nextLine().trim();
    }

    boolean found = false;

    for(int i = 0; i < portfolioAssets.size(); i++){
      if(portfolioAssets.get(i).getName().equalsIgnoreCase(searchName)){
        System.out.println("Asset found at position " + (i + 1) + ": ");
        portfolioAssets.get(i).displayInfo();
        found = true;
      }
    }

    if(!found){
      System.out.println("No asset found with the name \"" + searchName + "\".");
    }
  }

  public static void calculateSummary(){
    System.out.println("\n--- Portfolio Summary ---");

    if(portfolioAssets.isEmpty()){
      System.out.println("Your portfolio is empty. Add some assets first!");
      return;
    }

    double totalValue = 0.0;
    double totalReturn = 0.0;
    int totalRisk = 0;
    int stockCount = 0;
    int bondCount = 0;

    for(int i = 0; i < portfolioAssets.size(); i++){
      FinancialAsset asset = portfolioAssets.get(i);
      totalValue += asset.getCurrentValue();
      totalReturn += asset.calculateReturn();
      totalRisk += asset.getRiskRating();

      if(asset instanceof Stock){
        stockCount++;
      } else if (asset instanceof Bond){
        bondCount++;
      }
    }

    double averageRisk = (double) totalRisk / portfolioAssets.size();

    System.out.println("Total Assets:          " + portfolioAssets.size());
    System.out.println("  Stocks:              " + stockCount);
    System.out.println("  Bonds:               " + bondCount);
    System.out.printf("Total Portfolio Value:  $%.2f%n", totalValue);
    System.out.printf("Total Expected Return:  $%.2f%n", totalReturn);
    System.out.printf("Average Risk Rating:    %.1f/10%n", averageRisk);
  }

  public static void removeAsset(){
    System.out.println("\n--- Remove Asset ---");

    if(portfolioAssets.isEmpty()){
      System.out.println("Your portfolio is empty. Nothing to remove.");
      return;
    }

    viewPortfolio();

    int index = getValidInt("Enter the asset number to remove", 1, portfolioAssets.size());

    FinancialAsset toRemove = portfolioAssets.get(index - 1);
    System.out.print("Are you sure you want to remove \"" + toRemove.getName() + "\"? (y/n): ");
    String confirm = kb.nextLine().trim().toLowerCase();

    if(confirm.equals("y") || confirm.equals("yes")){
      portfolioAssets.remove(index - 1);
      System.out.println("Asset removed successfully!");
    } else{
      System.out.println("Removal cancelled.");
    }
  }

  public static int getValidInt(String prompt, int min, int max){
    int value;

    while(true){
      System.out.print(prompt + " (" + min + "-" + max + "): ");
      String input = kb.nextLine().trim();

      try{
        value = Integer.parseInt(input);

        if(value >= min && value <= max){
          return value;
        } else{
          System.out.println("Please enter a number between " + min + " and " + max + ".");
        }
      } catch(NumberFormatException e){
        System.out.println("Invalid input. Please enter a whole number.");
      }
    }
  }

  public static double getValidDouble(String prompt, double min, double max){
    double value;

    while(true){
      System.out.printf("%s (%.2f-%.2f): ", prompt, min, max);
      String input = kb.nextLine().trim();

      try{
        value = Double.parseDouble(input);

        if(value >= min && value <= max){
          return value;
        } else{
          System.out.printf("Please enter a number between %.2f and %.2f.%n", min, max);
        }
      } catch(NumberFormatException e){
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }
}
