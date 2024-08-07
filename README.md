# CSCB07Project TAAM
To get started as an Admin? Sign up or use:
> User: demo@demo.com
> Password: demodemo

## Requirements
This project was written for Android with API versions 34/35. Preferably using Pixel 7-8

## Features
### Users:
1. Scroll through the items in the museum
2. Viewing a specific item
3. Searching for specific items (More detail on this in the Admins section, but it is the same)
4. May choose to login/signup as an Admin (User Authentication)

### Admins:
Anything a User can do +
1. Can add a new item with:
   - a unique lot number
   - any number of image/videos
   - a category (can add or delete categories here)
   - a period (can add or delete period here)
2. Can search for items:
   - Use any combination of lot/name/category/period
   - Any boxes empty/none means no filter
   - Keyword search is included for name searching
3. Can generate a report for a specific:
   - Lot number
   - Name
   - Category (can also choose a report with description and picture only)
   - Period (can also choose a report with description and picture only)
   - All items!
5. Remove any number of selected items
6. Back button logs you out of Admin

## Testing Info [Read!]
The compatibility of running test seems to be very delicate and  PC specific.
Here is a reference of coverage running on my PC: 
<img src="https://github.com/user-attachments/assets/f3a06e96-ef62-46e3-af19-24b6fb792552" width="350" height="350">

❗If you run into issues with the Junit Tests, you may have to edit Configs:

![image](https://github.com/user-attachments/assets/a2c3d6dd-ee03-4505-807b-b23d1b200b38)

And then specify login package in "Packages and classes to include in coverage data":

<img src="https://github.com/user-attachments/assets/38c2b2f5-d0af-49d6-b2b9-53fde56a2955" width="350" height="350">

And try Cleaning & Rebuilding the project.

## Team Members
