# Money App
The project contains an Android money tracking application with features such as, Add Transaction and View Transaction History.

## Features
### Home Page
  ![Home Page](https://drive.google.com/uc?export=view&id=1gyn4BBfvUdOfSV3y9a0yS2qJSNxc4Vfw)
  
### Add Transaction Page
  - User can add transaction by inputting transaction's `amount`, `type`, `notes`*(optional)*, `category`, and `date` 
  - Transaction `category` dropdown list will change depending on selected transaction `type`

  ![Add Transaction Page](https://drive.google.com/uc?export=view&id=17H_NMWAlX1kQ-FDdF6fSSHN8SIP8FNyQ)
  
  [//]: # (pardon the nbsp)
  #### **Expense** category types &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; **Income** category types
  
  ![Add Transaction Page - Expense Categories](https://drive.google.com/uc?export=view&id=15v9UIWPVI-mVIQ5k3P_G-nMjVuTqAgT1)
  ![Add Transaction Page - Income Categories](https://drive.google.com/uc?export=view&id=1NpH2xbb_XLErRVMmxm6dKOAdrNUUznjT)

### View Transaction History Page
  - User will view all transaction history by default or pressing `reset filter`
  - Press `type & category` to only apply transaction `type` and `category` filter
    - Identical to **Add Transaction**, transaction `category` dropdown list will also change depending on selected transaction`type`
  - Press `date` to only apply `date` filter
  - Press `filter both` to apply both `type & category` and `date` filter
  - Press `reset filter` to remove all filters

  ![View Transaction History Page ](https://drive.google.com/uc?export=view&id=1Y9OrAPTV7y5vKivV84snJLcifjoBNZeN)
  
## Development Environment
The app is written entirely in Kotlin and uses the Gradle build system.

To build the app, use the `gradlew build` command or use "Import Project" inAndroid Studio.

## Database
This projects uses [Cloud Firestore](https://firebase.google.com/docs/firestore/) for the database. 

In the current version of the app, user is only able to view existing data, unable to add new transactions.
