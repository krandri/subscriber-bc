Subscriber-bc
-

    Mini projet de gestion d'abonnés dans le cadre d'un test technique pour le groupe Canal.

    Il s'agit d'une application Spring Boot simple dans le but de créer une API REST.
    
    Architecture microservices classique avec Controller -> Service -> Repo 
    
    DTO et Entités
    
    Mise en place de spécifications pour la partie recherche

    Authentification

Pour lancer le projet :
-

Le projet se compile en Java 17.

Il y a un script de création au démarrage de l'appli avec l'implémentation de Liquibase. Il faut modifier le
script afin d'ajouter des données, ou bien créer directement des données au démarrage.

L'application fonctionne en BasicAuth. Il y a un compte qui fonctionne, les deux autres non (pour test) :

    usr : admin 
    pwd : adminPass

Création : [POST /subscribers]()
-

```json
{
  "fname": "Kevin",
  "lname": "Randri",
  "mail": "test@mail.com",
  "phone": "066060606"
}
```

Si un abonné existe déjà (identifié par le même mail et numéro de téléphone), on envoie une 409 disant que l'abonné
existe déjà.

Recherche d'un (ou plusieurs) abonnés : [GET /subscribers?search=clé:valeur,clé:valeur]()
-

La recherche fonctionne avec des critères séparés avec une virgule, formattés de la manière suivante :
clé,opérateur,valeur.

Voici les opérateurs possibles que la regex peut capter. Cependant, un seul n'a été implémenté :

- Opérateur de supériorité >
- Opérateur d'infériorité <
- Deux points :, le seul qui fonctionne, équivaut à =

Exemple de critère : ***fname:Kevin***

Exemples :

- [Rechercher par abonné inactif](http://localhost:8080/subscribers?search=isActiv:false)
- [Rechercher par nom](http://localhost:8080/subscribers?search=lname:Randri)
- [Rechercher par prénom et par abonné actif](http://localhost:8080/subscribers?search=isActiv:true,fname:Kevin)

Résiliation d'un abonné : [POST /subscribers/{id}/cancel]()
-
Permet de passer un abonné à inactif.

Mise à jour d'un abonné : [PUT /subscribers/{ID}/update]()
-

Permet de mettre à jour un abonné. La mise à jour partielle est mise en place, donc il est possible de renvoyer un JSON
ne contenant pas toutes les valeurs.


Axes d'amélioration :
-

- Ajout de MapStruct pour faciliter le mapping
- Changement de la sécurisation (Bearer token /JWT)
- Mise en place de fonctions génériques pour les Controllers / Services
- Revoir les Specifications pour la recherche (utilisation du Metamodel)
- Tests d'intégration pour les tests du Controller
- Ajouter des tests sur la validation lors de la création
