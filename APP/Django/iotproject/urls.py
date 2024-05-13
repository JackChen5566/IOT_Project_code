from django.contrib import admin
from django.urls import path
from myapp import views

urlpatterns = [
    #page
    path('admin/', admin.site.urls),
    path('', views.index, name='index'),
    path('database_view/', views.database_view),
    path('main_view/', views.main_view),


    #functions
    path('<int:led>/', views.toggle, name='toggle'),
    path('control_servo/', views.control_servo, name='control_servo'),
    path('WaterPump/',views.pump_run,name='WaterPump'),
    path('set_ph_value/',views.set_ph_value,name='set_ph_value'),
    path('temperature_set/', views.temperature_data),
    path('temperature_get/', views.read_temp_data),
    path('ph_get/', views.read_ph_data),
    path('temperature_gets/', views.read_temp_datas),
    path('ph_gets/', views.read_ph_datas),

    path('indexcart/', views.indexcart),
    path('detail/<int:productid>/', views.detail),
    path('addtocart/<str:ctype>/', views.addtocart),
    path('addtocart/<str:ctype>/<int:productid>/', views.addtocart),
    path('cart/', views.cart),
    path('cartorder/', views.cartorder),
    path('cartok/', views.cartok),
    path('cartordercheck/', views.cartordercheck),
    ]