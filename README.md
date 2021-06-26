# Projekt_NWTIS
Projekt napravljen na kolegiju Napredne web tehnologije i servisi

U projektu se radi o dohvaćanju podataka o letovima s aerodroma putem OpenSky web servisa i meteo podataka za aerodrome putem OpenWeatherMap web servisa.

Projekt se sastoji od 4 aplikacije.
Prva aplikacija služi kao autentikacijski i autorizacijski servis i pruža poslužitelj na socketu na koji dolaze određene komande putem kojih se autenticiraju i autoriziraju korisnici.
Druga aplikacija pruža RESTful servis i dohvaća podatke o aerodromima.
Treća aplikacija pruža korisničko sučelje za rad s pravima korisnika i rad s aerodromima koje prate korisnici i za koje se dohvaćaju podaci.
Četvrta aplikacija je Enterprise aplikacija koja služi za pregled podataka o korisnicima i za prikaz letova i meteoroloških podataka za aerodrome koje prati prijavljeni korisnik.
