# Portfolio Tracker
This application allows an user to select up to 5 stocks on the home page, giving an option to input their preferred stock symbols. Design of the application can be found [here](https://www.figma.com/file/FyPxJ4ZZ2e5ZtB8X2a0Cy2/CS205-Stock-portfolio).

## Features
**Data Retrieval**  
Upon clicking the *Download* button, data is fetched in the background via a Service (background thread) to [finnhub](https://finnhub.io/). The data is then stored in a local SQLite database using a ContentProvider.

**Performance Metrics**  
Upon clicking the Calculate button, two metrics will be computed for a specific ticker, namely annualized return and volatility. Annualized return is calculated as AR = N * ave(r), where N is the number of periods in the year and ave(r) is the average return for the period. Annualized volatility is calculated as AV = sqrt(N) * sd(r), where N is the number of periods in a year and sd(r) is the standard deviation of returns for a period. Calculations are done by querying the database for the ticker values and performing arithmetic operations.

## Design
**Threads**  
A Broadcast Receiver with three Intent Filters is spawned upon entering the home page. Two filters handle the data downloads in the background, notifying if the download is completed or failed, whereas the last filter handles the computation of metrics. This ensures that the main thread is not blocked, allowing users to download data or calculate values for multiple stock symbols, as the processes are executing in the background.

**Interactions**  
All user interactions are handled by the main activities which comprise two fragments - Introduction & Home. Fragments are implemented for better performance to save memory overhead from transitioning between activities as well as to provide modularity, allowing the reusing of components in multiple activities.

**Communication of Processes**  
Main activity transits between the two fragments as mentioned above. User interactions with the download buttons on the Home fragment start a Service, delivering messages to the Looper's message queue that are received by the Broadcast Receiver, which then updates the UI. Pressing the Calculate button broadcasts an intent that is received by the Broadcast Receiver, performing the calculations and updating the values on the UI.

## App Requirements
- Device: Nexus 6P
- API: Level 30
- Resolution 1440 x 2560: 560 dpi
- Multi-Core CPU 4
- RAM 1536 MB
- SD card 512 MB
