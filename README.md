# ☀ MathSolar ![](https://img.shields.io/static/v1?style=plastic&label=Version&labelColor=212121&message=Beta&color=green) ![](https://img.shields.io/static/v1?style=plastic&label=Language&labelColor=212121&message=Kotlin&color=9719ff) ![](https://img.shields.io/static/v1?style=plastic&label=Technology&labelColor=212121&message=Android&color=#a4c639) ![](https://img.shields.io/static/v1?style=plastic&label=Backend&labelColor=212121&message=Firebase&color=ff9819) ![](https://img.shields.io/static/v1?style=plastic&label=Layout&labelColor=212121&message=XML&color=ff0068)

![](resources/images/mathsolar_cover.jpg?raw=true)
> 
> This is a project I am developing as part of my thesis to graduate from my bachelor's degree in May. It is an application capable of performing photovoltaic sizing with and without batteries. In addition, it brings with it different functionalities, calculators, teaching topics, news and more! It will soon be available on the PlayStore. 💪

## Table of Contents
* [General Infomation](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Architecture](#architecture)
* [Screenshots](#screenshots)
* [Material Theming](#material-theming)
* [Performance](#performance)
* [Setup](#setup)
* [Project Status](#project-status)
* [Room for Improvement](#room-for-improvement)
* [Acknowledgements](#acknowledgements)
* [Contact](#contact)
<!-- * [License](#license) -->

## General Information
This Android application is developed with the Kotlin language within the Android Studio development environment. Currently, it is in the process of being completed but I'm well on my way. It has the possibility for users to create an account in the cloud by the Firebase Backend service. In addition, it has five different sections that will be of importance and very useful for all those who are interested in photovoltaic energy and the environment. 

The first section consists of news and articles about solar energy, sustainability, and the environment. Then, the projects section where the user will be able to size a photovoltaic system with or without batteries. Within the project details, an energy and economic analysis is displayed to let the user know how feasible it is to implement it. The calculator section is self-explanatory. Then, the tools section will consist of offering multiple useful gadgets for the photovoltaic systems technician. It is being considered whether to implement it or leave it for later. Finally, the learning section is where all the necessary information will be concentrated so that a person who does not know about these topics can get into the subject in a matter of minutes. 

So, broadly speaking, this would be a very general description of the application. 

https://user-images.githubusercontent.com/65273835/148716870-e00af611-e769-472f-93d0-7f4a50f47456.mp4

![](https://img.shields.io/static/v1??style=flat-squaren&label=Language&labelColor=212121&message=Kotlin&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=IDE&labelColor=212121&message=AndroidStudio&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Architecture&labelColor=212121&message=MVVM&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Network&labelColor=212121&message=Retrofit2&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Serialization&labelColor=212121&message=GSON&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=ImageLoading&labelColor=212121&message=Glide&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Cache&labelColor=212121&message=Room&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Preferences&labelColor=212121&message=Datastore&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Injection&labelColor=212121&message=Hilt&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Navegation&labelColor=212121&message=NavegationComponents&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Views&labelColor=212121&message=Databinding&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Background&labelColor=212121&message=Workmanager&color=9719ff)
![](https://img.shields.io/static/v1??style=flat-squaren&label=API&labelColor=212121&message=GoogleMaps&color=2132CE)
![](https://img.shields.io/static/v1??style=flat-squaren&label=API&labelColor=212121&message=FreeNews&color=2132CE)
![](https://img.shields.io/static/v1??style=flat-squaren&label=API&labelColor=212121&message=NasaPower&color=2132CE)
![](https://img.shields.io/static/v1??style=flat-squaren&label=API&labelColor=212121&message=PvWatts&color=2132CE)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Firebase&labelColor=212121&message=Authentification&color=ff9819)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Firebase&labelColor=212121&message=Store&color=ff9819)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Firebase&labelColor=212121&message=Storage&color=ff9819)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Firebase&labelColor=212121&message=Crashlytics&color=ff9819)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Firebase&labelColor=212121&message=TestLab&color=ff9819)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Permissions&labelColor=212121&message=Camara&color=a4c639)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Permissions&labelColor=212121&message=ReadInternalStorage&color=a4c639)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Permissions&labelColor=212121&message=Location&color=a4c639)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Layout&labelColor=212121&message=XML&color=ff0068)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Design&labelColor=212121&message=Figma&color=ff0068)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Guide&labelColor=212121&message=Material2&color=ff0068)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Animations&labelColor=212121&message=Lottie&color=ff0068)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Animations&labelColor=212121&message=MotionLayout&color=ff0068)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Transitions&labelColor=212121&message=Motion&color=ff0068)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Theme&labelColor=212121&message=Light&color=ff0068)
![](https://img.shields.io/static/v1??style=flat-squaren&label=Theme&labelColor=212121&message=Dark&color=ff0068)

## Technologies used
- Android - version beta.

## Features
- 👤 Log as a guest. If you have favorite articles or projects, when you create your user they are saved in the cloud.
- ☁ Your favorite articles and your projects are stored in the cloud.
- 💰 Find out how much money you will save with the project.
- 📍 From the location, you can estimate the production of your system with radiation and temperature data.
- 🤩 You can create as many PV projects as you want.
- 🌤 Create sizing with or without batteries.
- 📰 Find out the latest news about solar panels and the environment in the articles section. 
- 🗃 You can save your favorite articles in the cloud. It also records the ones you have already seen.
- 📚 Become an energy expert with the various calculators and topics we have for you. 
- 🌙 Dark mode available.
- 🌟 and more!

## Architecture
![](resources/images/architecture.mvvm.png?raw=true)

## Screenshots
| Login | Articles |  Article Detail |
|:-:|:-:|:-:|
| ![Fist](resources/images/00.login_light.jpg?raw=true) | ![2](resources/images/01.articles_light.jpg?raw=true) | ![3](resources/images/02.article_light.jpg?raw=true) |
| ![4](resources/images/00.login_dark.jpg?raw=true) | ![5](resources/images/01.articles_dark.jpg?raw=true) | ![6](resources/images/02.article_dark.jpg?raw=true) |


## Material Theming
MathSolar uses Material Theming to customize the app’s color, typography, shape and motion.

### Color
Color is a very important issue. I based it on the colors of the brand, that is to say; the color palette of the logo. For the light theme, I was looking for something bright, that would be able to portray the brightness of the sun. Yellow was the perfect color, especially one that was close to amber. The blue refers to two things: the beautiful sky during the day and the characteristic color of polycrystalline solar panels. In the end I had it pretty clear, so, based on that, I started experimenting with what colors would fit the application well. 

![](resources/images/08.color.theme.light.articles.jpg?raw=true)

On the other hand, for the dark theme, I was looking for something more subdued and peaceful, more appropriate for the night. It was here that I experimented with shades of blue for different purposes. As for yellow, I used it to emphasize some details. 

![](resources/images/08.color.theme.dark.articles.jpg?raw=true)

MathSolar’s color palette is defined in [color.xml][color.xml] and applied globally via the app’s [default][theme.default] and [dark][theme.dark] themes.

### Tipography

![](resources/images/09.typography.theme.jpg?raw=true)

MathSolar uses [Ubuntu][ubuntu.link] and [Barlow][barlow.link] as its typeface. All items in the type scale provide the typographic variety necessary for MathSolar's content. See [type.xml][type.xml] which defines `TextAppearances` which are then [set in the theme][theme.default] and referred to using `?attr/textAppearance[...]` throughout.

### Shape

![](resources/images/10.shape.theme.jpg?raw=true)

MathSolar defines small, medium, and large shape categories for different sized components.

### Motion
#### Container transform
The container transform pattern is designed for transitions between UI elements that include a container. This pattern creates a visible connection between two UI elements.
MaterialContainerTransform is a shared element transition. Unlike traditional Android shared elements, it is not designed around a singular piece of shared content, such as an image, to be moved between two scenes. Instead, the shared element here refers to the bounding container of a start View or ViewGroup (e.g. the entire row layout of an item in a list) transforming its size and shape into that of an end View or ViewGroup (e.g. the root ViewGroup of a full screen Fragment). These start and end container Views are the “shared element” of a container transform. While these containers are being transformed, their contents are swapped to create the transition.

https://user-images.githubusercontent.com/65273835/148717250-d088bd7b-77b2-4619-b830-4408858682bd.mp4

https://user-images.githubusercontent.com/65273835/148717679-8b199974-fccb-4e62-a7c5-b98e7ebddda8.mp4

#### Shared axis
The shared axis pattern is used for transitions between UI elements that have a spatial or navigational relationship. This pattern uses a shared transformation on the x, y, or z axis to reinforce the relationship between elements.

https://user-images.githubusercontent.com/65273835/148717782-b5d0d3d4-5498-477d-b45d-a0bfa41ef240.mp4

https://user-images.githubusercontent.com/65273835/148717789-e3074b93-1df4-4065-8798-c824d80de0ba.mp4

https://user-images.githubusercontent.com/65273835/148717796-2d693c30-c3ad-442d-b94e-48a982d5f4c5.mp4

#### Fade through
The fade through pattern is used for transitions between UI elements that do not have a strong relationship to each other.

https://user-images.githubusercontent.com/65273835/148717881-6f9d67b5-b667-46d7-98f7-efd78a343d2a.mp4

## Performance
Performance is a very important issue to improve the user experience for everyone, that is; those who have in their hands a high-end and low-end mobile. Therefore, lately I have been very interested in improving the performance of my application and be aware of the different indicators that can affect the performance. For now, overdrawing is something I'm keeping an eye on to avoid and, also, from time to time I analyze the CPU memory and how it affects the battery.

### CPU Memory



### Overdrawing
Android colors UI elements to identify the level of overlap as follows:
-  True color: No overlap
- 🔵 Blue: Overlapped 1 time
- 🟢 Green: Overlapped 2 times
- 🎀 Pink: Overlapped 3 times
- 🔴 Red: Overlapped 4 or more times

| Articles | Article Detail | Average consumption |
|:-:|:-:|:-:|
| ![Fist](resources/images/04.overdraw_00.jpg?raw=true) | ![2](resources/images/06.overdraw_02.jpg?raw=true) | ![3](resources/images/07.overdraw_03.jpg?raw=true) |

As we can appreciate in the list of items, the envelope drawn is minimal, being in most of the items a blue color. This is a good practice because the cards as the activity lacks unnecessary backgrounds that may affect the performance. 

Subsequently, in the item details, there is a green drawn envelope. The reason is because the content is inside a NestedScroll containing a CardView. The NestedScroll is necessary for the user to be able to visualize all the content of the news item. The CardView is for mere taste, since it gives the appearance as if it were a layer above the background and makes it look better. However, the image is red. The reason is because the image is inside a CardView to round the edges. I tried to use the ShapeableImageView view but every time I went back to the article fragment, I saw its resulting edges in black color. It did not keep the round shape. And, in fact, the CardView doesn't either, but at least they don't look black. I also tried defining a round-edged drawable for the image, but that didn't work either. Anyway, I will continue to investigate how to solve this problem. Maybe with Clipping. 

Finally, there is the fragment of the average consumption. Like the details in the article, this whole section is inside a NestedScroll containing a CardView. Fortunately, the TextInputLayouts are pink.  

## Setup
![](https://img.shields.io/static/v1?style=plastic&label=Kotlin&labelColor=212121&message=1.5&color=9719ff) ![](https://img.shields.io/static/v1?style=plastic&label=MinSDKVersion&labelColor=212121&message=8.0&color=#a4c639) ![](https://img.shields.io/static/v1?style=plastic&label=AndroidStudio&labelColor=212121&message=4.2&color=green)

* For security reasons you must add your own Firebase configuration file `google-services.json` with Auth, Storage, FireStore, Crashlytics and Analytics active and the Google Maps API access key.
* It is sufficient to open and run the project from Android Studio.


## Project Status
Project is: _in progress_

The project is still in progress. I have an estimate to finish it before May because it must be this way for sure. As soon as I finish it, I will upload it to the Play Store.

## Room for Improvement

## Acknowledgements

## Contact

[color.xml]: https://github.com/CalixtoElProgramador/MathSolar-Android/blob/master/app/src/main/res/values/color.xml
[ubuntu.link]: https://fonts.google.com/specimen/Ubuntu?query=Ubuntu
[barlow.link]: https://fonts.google.com/specimen/Barlow?query=Barlow
[theme.default]: https://github.com/CalixtoElProgramador/MathSolar-Android/blob/master/app/src/main/res/values/themes.xml
[theme.dark]: https://github.com/CalixtoElProgramador/MathSolar-Android/blob/master/app/src/main/res/values-night/themes.xml
[type.xml]: https://github.com/CalixtoElProgramador/MathSolar-Android/blob/master/app/src/main/res/values/type.xml




