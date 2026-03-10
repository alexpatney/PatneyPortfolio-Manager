import java.util.ArrayList;
import java.util.Scanner;

/**
* PortfolioManager is the runner class that contains main() and all the menu logic.
* It manages an ArrayList<FinancialAsset> which stores both Stock and Bond objects
* in a single collection. When you iterate through the list and call calculateReturn()
* or displayInfo(), Java will automatically dispatch to the correct child version based
* on the actual object type, even though the declared type is the parent FinancialAsset.
* The program runs a menu loop with options to add, view, sort, search, summarize, and
* remove assets. Every user input is validated through helper methods so the program
* never crashes on unexpected inputs.
*/
public class PortfolioManager{
  // This is the central data structure of the program, one ArrayList holding both Stocks
  // and Bonds. Using the parent type FinancialAsset as the generic allows for polymorphism.
  private static ArrayList<FinancialAsset> portfolioAssets = new ArrayList<FinancialAsset>();
  // A single shared scanner for all console input throughout the program
  private static Scanner kb = new Scanner(System.in);

  /**
  * Displays a welcome message and runs the main menu loop until the user chooses to exit.
  * Each menu option routes to a dedicated method to keep the main() method clean and organized.
  */
  public static void main(String[] args){
    System.out.println("==========================================");
    System.out.println("   Welcome to PatneyPortfolio Manager");
    System.out.println("==========================================");

    boolean running = true;

    // Main menu loop, keeps running until the user selects Exit (option 7)
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

      // Each case calls a separate method to keep the code organized
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

  /**
  * Prompts the user to add either a Stock or a Bond to the portfolio.
  * Every input is validated, numbers must be within a sensible range, and
  * the program reprompts instead of crashing if the user types something invalid.
  * The new asset is stored in the ArrayList<FinancialAsset>, showing how child
  * objects (Stocks or Bonds) can be stored in a parent-typed collection.
  */
  public static void addAsset(){
    System.out.println("\n-- Add New Asset ---");
    System.out.println("What type of asset?");
    System.out.println("1. Stock");
    System.out.println("2. Bond");

    int type = getValidInt("Enter asset type", 1, 2);

    // These attributes are shared by both Stock and Bond (inherited from FinancialAsset)
    System.out.print("Enter asset name: ");
    String name = kb.nextLine().trim();

    // Validates that the name is not empty so it has something to search by
    while(name.isEmpty()){
      System.out.print("Name cannot be empty. Enter asset name: ");
      name = kb.nextLine().trim();
    }

    double currentValue = getValidDouble("Enter current value ($)", 0.01, 1000000000.0);
    int riskRating = getValidInt("Enter risk rating (1-10)", 1, 10);

    if(type == 1){
      // Collects stock specific information
      System.out.print("Enter ticker symbol: ");
      String tickerSymbol  = kb.nextLine().trim().toUpperCase();

      while(tickerSymbol.isEmpty()){
        System.out.print("Ticker cannot be empty. Enter ticker symbol: ");
        tickerSymbol = kb.nextLine().trim().toUpperCase();
      }

      int sharesHeld = getValidInt("Enter shares held", 1, 1000000000);
      double pricePerShare = getValidDouble("Enter price per share ($)", 0.01, 1000000.0);
      double dividendYield = getValidDouble("Enter dividend yield (e.g., 0.02 for 2%)", 0.0, 1.0);

      // Create the Stock and add it to the ArrayList
      Stock newStock = new Stock(name, currentValue, riskRating,
                                 tickerSymbol, sharesHeld, pricePerShare, dividendYield);
      portfolioAssets.add(newStock);
      System.out.println("Stock \"" + name + "\" added successfully!");

    } else{
      // Collects bond specific information
      double faceValue = getValidDouble("Enter face value ($)", 0.01, 1000000000.0);
      double couponRate = getValidDouble("Enter coupon rate (e.g., 0.05 for 5%)", 0.0, 1.0);
      int yearsToMaturity = getValidInt("Enter years to maturity", 1, 100);

      // Create the Bond and add it to the ArrayList
      Bond newBond = new Bond(name, currentValue, riskRating,
                              faceValue, couponRate, yearsToMaturity);
      portfolioAssets.add(newBond);
      System.out.println("Bond \"" + name + "\" added successfully!");
    }
  }

  /**
  * Displays all the assets in the portfolio by calling displayInfo() on each one.
  * This is the core demonstration of polymorphism, it iterates through the
  * ArrayList<FinancialAsset> and call displayInfo(), but Java will dispatch to
  * Stock.displayInfo() or Bond.displayInfo() based on the actual runtime type.
  * Never need to check what type each object is, the behavior changes automatically.
  */
  public static void viewPortfolio(){
    System.out.println("\n--- Your Portfolio ---");

    if(portfolioAssets.isEmpty()){
      System.out.println("Your portfolio is empty. Add some assets first!");
      return;
    }

    // Polymorphic loop. displayInfo() resolves to the correct child version at runtime.
    for(int i = 0; o < portfolioAssets.size(); i++){
      System.out.println("Asset #" + (i + 1) + ": ");
      portfolioAssets.get(i).displayInfo();
    }
  }

  /**
  * Sorts the portfolio using selection sort based on the user's chosen criteria.
  * Selection sort was chosen because it is a key computer science algorithm. It finds
  * the minimum or maximum element in the unsorted portion and swaps it into place. The
  * user can sort by current value, expected return, or risk rating. This method also
  * demonstrates polymorphism as calculateReturn() is called on each FinancialAsset
  * reference and dispatches to the correct child implementation.
  */
  public static void sortPortfolio(){
    System.out.println("\n--- Sort Portfolio ---");

    if(portfolioAssets.size() < 2){
      System.out.println("Need at least 2 assets to sort.");
      return;
    }

    System.out.println("Sort by:");
    System.out.println("1. Current Value (low to high)");
    System.out.println("2. Expected Return (high to low)");
    System.out.println("3. Risk Rating (low to high)");

    int sortChoice = getValidint("Enter sort criteria", 1, 3);

    // Selection Sort. On each pass, find the element that belongs in position i and swap it there.
    for(int i = 0; i < portfolioAssets.size() - 1; i++){
      int targetIndex = i;

      for(int j = i + 1; j < portfolioAssets.size(); j++){
        boolean shouldSwap = false;

        if(sortChoice == 1){
          // Sort by current value ascending, smallest value first
          shouldSwap = portfolioAssets.get(j).getCurrentValue() < portfolioAssets.get(targetIndex).getCurrentValue();
        } else if(sortChoice == 2){
          // Sort by expected return descending, highest return first
          shouldSwap = portfolioAssets.get(j).calculateReturn() > portfolioAssets.get(targetIndex).calculateReturn();
        } else{
          // Sort by risk rating ascending, lowest risk first
          shouldSwap = portfolioAssets.get(j).getRiskRating() < portfolioAssets.get(targetIndex).getRiskRating();
        }

        if(shouldSwap){
          targetIndex = j;
        }
      }

      // Swap the found element into its correct position
      if(targetIndex != i){
        FinancialAsset temp = portfolioAssets.get(i);
        portfolioAssets.set(i, portfolioAssets.get(targetIndex));
        portfolioAssets.set(targetIndex, temp);
      }
    }

    System.out.println("Portfolio sorted successfully!");
    viewPortfolio();
  }

  /**
  * Searches for an asset by name using linear search.
  * Linear search checks each element ony by one until a math is found or the
  * end of the list is reached. This is appropriate here because the list is
  * unsorted by name and relatively small. The search is case insensitive so
  * the user doesn't have to math the exact capitalization they used when adding.
  */
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

    // Linear search algorithm here, check each asset's name one by one
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

  /**
  * Calculates and displays a summary of the entire portfolio, including total
  * value, total expected return, average risk rating, and the count of each
  * asset type. This method demonstrates polymorphism through calculateReturn()
  * as each asset's return is computed using its own overridden formula (dividend
  * income for stocks, coupon payments for bonds), but the code treats them
  * all uniformly through the parent FinancialAsset type.
  */
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

    // Single loop processes all assets polymorphically
    for(int i = 0; i < portfolioAssets.size(); i++){
      FinancialAsset asset = portfolioAssets.get(i);
      totalValue += asset.getCurrentValue();
      totalReturn += asset.calculateReturn();
      totalRisk += asset.getRiskRating();

      // Count asset types using instanceof to provide a type breakdown
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

  /**
  * Removes an asset from the portfolio by showing all assets and letting the
  * user pick which to remove by number. The viewPortfolio() call shows the
  * numbered list so the user knows what they're removing before confirming.
  */
  public static void removeAsset(){
    System.out.println("\n--- Remove Asset ---");

    if(portfolioAssets.isEmpty()){
      System.out.println("Your portfolio is empty. Nothing to remove.");
      return;
    }

    // Show the portfolio so the user can see the numbered assets
    viewPortfolio();

    int index = getValidInt("Enter the asset number to remove", 1, portfolioAssets.size());

    // Confirm before removing to prevent accidental deletions
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

  /** Validates integer input from the user, ensuring it falls within the specified range.
  * If the user types something that isn't a number like letters or symbols, or types a
  * numer outside the valid range, the method reprompts until valid input is provided. This
  * prevents InputMismatchException and keeps the program from crashing on bad unput.
  * This helper method is reused everywhere an integer is needed throughout the program.
  */
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
        // User typed something that isn't an integer like letters or symbols
        System.out.println("Invalid input. Please enter a whole number.");
      }
    }
  }

  /**
  * Validates double input from the user, ensuring it falls within the specified
  * range. Works the same way as getValidInt but for decimal values. This is used
  * for monetary values like price, face value, and yield percentages. Prevents
  * NumberFormatException by catching bad input and reprompting.
  */
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
        // User typed something that isn't a number
        System.out.println("Invalid input. Please enter a valid number.");
      }
    }
  }
}
