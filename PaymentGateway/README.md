
# Payment Gateway Integration

This is a simple Android application that demonstrates Razorpay Payment Gateway integration. The app contains a single button that, when clicked, initiates the payment process using Razorpay Checkout.

## 📌Features

- Simple UI with a single button for payment.
- Razorpay Checkout integration for seamless transactions.
- Supports multiple payment methods (UPI, Cards, Net Banking, Wallets, etc.).


## 🛠️Technologies Used

**Languages:** Kotlin/Java

**SDK:** Razorpay Android SDK (com razorpay:checkout:1.6.40)

**IDE:** Android Studio




## 🚀How It Works

- Click the "Pay Now" button.
- Razorpay Checkout opens for payment.
- Complete the payment using any preferred method.
- Receive a success or failure response.

**Note:** Uses Razorpay Test API Key for testing purposes.


## 🔑Razorpay Test API Key
This project uses the Razorpay Test Mode API Key, which allows you to test transactions without real payments.

- You can find your test keys on the [Razorpay Dashboard](https://razorpay.com/?utm_source=google&utm_medium=cpc&utm_campaign=RPSME-RPPerf-GSearchBrand-Prospect-DWeb-No-Competition&utm_adgroup=All-Combination&utm_content=BrandAd&utm_term=razorpay%20dashboard&utm_gclid=&utm_campaignID=21555097091&utm_adgroupID=168130000920&utm_adID=708304791490&utm_network=g&utm_device=c&utm_matchtype=e&utm_devicemodel=&utm_adposition=&utm_location=9040229&gad_source=1&gclid=Cj0KCQiA_NC9BhCkARIsABSnSTZHW-Zg-4OT8iqmFhh28p9m5mbk7TtgMdpn2r1UIUNUwnUo9VPY4MMaAiGsEALw_wcB).
- Replace the Test Key with a Live Key before deploying the app.

```kotlin
val razorpay = Checkout()
razorpay.setKeyID("rzp_test_XXXXXXXXXXXXXXXX") // Replace with your actual Test Key
```
## 📌Setup Instructions

1: Clone this repository

```bash
  git clone https://github.com/VikramSingh151/Android-Development.git
```

2: Open the project in Android Studio.

3: Add the Razorpay dependency in **build.gradle** (Module):

```gradle
  implementation 'com.razorpay:checkout:1.6.40'
```

4: Initialize Razorpay Checkout in your Activity/Fragment.

5: Run the project on a real device or emulator.


# 📸Screenshots


| Screenshot 1 | Screenshot 2 |
|-------------|-------------|
| ![Image](https://github.com/user-attachments/assets/0c0731dd-3d69-474b-81df-f705a8e78e30) | ![Image](https://github.com/user-attachments/assets/fc52c130-16d7-427c-a4db-b3788cc1a852) |

## License

[MIT](https://choosealicense.com/licenses/mit/)


