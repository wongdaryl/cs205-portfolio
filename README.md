# Portfolio Tracker
This application allows an user to select up to 5 stocks on the home page, giving an option to input their preferred stock symbols. Design of the application can be found [here](https://www.figma.com/file/FyPxJ4ZZ2e5ZtB8X2a0Cy2/CS205-Stock-portfolio).

## Features
**Data Retrieval**  
Upon clicking the *Download* button, data is fetched in the background via a Service (background thread) to [finnhub](https://finnhub.io/)

**Performance Metrics**  
Two metrics will be computed, namely annualized return and volatility. Annualized return is calculated based on a formula - N * average(r), where N is the number of periods in the year and average(r) is the average return for the period. Similarly, annualized volatility is calculated based on a formula - sqrt(N) * sd(r), where N is the number of periods in the year, sd(r) is the standard deviation of returns for a period.

## Design
**Threads**  
Three broadcast receivers are spawned upon entering the home page, of which two handles the data downloads in the background, notifying if the download is completed or failed. Another broadcast receiver handles the computation of metrics, ensuring that it does not block the whole application, allowing users to download data for other stock symbols while the computation of metrics is executing in the background.

**Interactions**  
All user interactions are handled by the main activities which compromises of 2 fragments - Introduction & Home. Fragments are implemented for better performance to save the memory overhead of transitioning between activities as well as providing modularity, allowing reusal of component in multiple activities.

**Communication of Processes**  
Main activity transits between the two fragments as mentioned above. User interactions with the buttons on the home fragment creates a Service which deliver messages to the Looper's message queue which are received by the Broadcast Receivers that updates the UI

## App Requirements
- Device: Nexus 6P
- API: Level 30
- Resolution 1440 x 2560: 560 dpi
- Multi-Core CPU 4
- RAM 1536 MB
- SD card 512 MB
