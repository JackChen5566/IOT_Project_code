from django.db import models



# Create your models here.
class ph_states(models.Model):
    ID = models.AutoField(primary_key=True)
    PH = models.FloatField(blank=True, null=True)
    Timestamp = models.DateTimeField(auto_now_add=True)


class TemperatureData(models.Model):
    ID = models.AutoField(primary_key=True)
    Temperature = models.CharField(max_length=20,blank=False)
    Timestamp = models.DateTimeField(auto_now_add=True)

class phdata(models.Model):
    ID = models.AutoField(primary_key=True)
    PH_value= models.CharField(max_length=20,blank=False)
    Timestamp = models.DateTimeField(auto_now_add=True)

class leddata(models.Model):
    ID = models.AutoField(primary_key=True)
    led_number= models.CharField(max_length=20,blank=False)
    state= models.CharField(max_length=20,blank=False)
    Timestamp = models.DateTimeField(auto_now_add=True)


class feeddata(models.Model):
    ID = models.AutoField(primary_key=True)
    run_value= models.CharField(max_length=20,blank=False)
    Timestamp = models.DateTimeField(auto_now_add=True)

class pumpdata(models.Model):
    ID = models.AutoField(primary_key=True)
    run_value= models.CharField(max_length=20,blank=False)
    Timestamp = models.DateTimeField(auto_now_add=True)

class ProductModel(models.Model):
    pname =  models.CharField(max_length=100, default='')
    pprice = models.IntegerField(default=0)
    pimages = models.CharField(max_length=100, default='')
    pdescription = models.TextField(blank=True, default='')
    def __str__(self):
        return self.pname
        
class OrdersModel(models.Model):
    subtotal = models.IntegerField(default=0)
    shipping = models.IntegerField(default=0)
    grandtotal = models.IntegerField(default=0)
    customname =  models.CharField(max_length=100, default='')
    customemail =  models.CharField(max_length=100, default='')
    customaddress =  models.CharField(max_length=100, default='')
    customphone =  models.CharField(max_length=100, default='')
    paytype =  models.CharField(max_length=50, default='')
    def __str__(self):
        return self.customname
     
class DetailModel(models.Model):
    dorder = models.ForeignKey('OrdersModel', on_delete=models.CASCADE)
    pname = models.CharField(max_length=100, default='')
    unitprice = models.IntegerField(default=0)
    quantity = models.IntegerField(default=0)
    dtotal = models.IntegerField(default=0)
    def __str__(self):
        return self.pname
    

