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

### Tipography

![](resources/images/09.typography.theme.jpg?raw=true)

### Shape

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










