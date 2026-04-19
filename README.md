# Skyjo

## Contexte du projet

Skyjo est un jeu de cartes multijoueur implémenté en Java dans le cadre du cours INF2050
à l'UQAM. Le jeu supporte de 2 à 4 joueurs humains ou robotiques, avec une interface
textuelle en ligne de commande.

Le projet respecte une architecture MVC et inclut des tests unitaires, des tests
de mutation et des tests d'intégration basés sur des joueurs robotiques déterministes.

## Documentation

La documentation Javadoc complète est disponible ici :
[Consulter la Javadoc]()

## Téléchargement

Téléchargez la dernière version du JAR ici :
[Télécharger skyjo.jar]()

## Instructions d'exécution

### Prérequis
- Java 23 ou supérieur

### Lancer le jeu

```bash
java -jar Skyjo.jar nom1 nom2
```

### Joueurs robotiques

Utilisez les noms réservés suivants pour remplacer un joueur par un robot :

| Nom      | Comportement                                   |
|----------|------------------------------------------------|
| `Keksli` | Choisit toujours la première option (Débutant) |
| `MadMax` | Choisit une option aléatoire (Intermédiaire)   |
| `S38`    | AI (Expert)                                    |

Exemple avec un humain contre un robot :

```bash
java -jar Skyjo.jar nom1 Keksli
```

Exemple avec quatre robots :

```bash
java -jar Skyjo.jar Keksli MadMax Keksli S38
```

Exemple avec une seed :

```bash
java -jar Skyjo.jar 5 nom1 nom2 nom3 nom4
```

## Contributeurs

- Joël Stéphane Tchiengang Nchuisseu
- Hasmik Tadevosyan