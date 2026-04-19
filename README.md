# Skyjo

## Contexte du projet

Skyjo est un jeu de cartes multijoueur implémenté en Java dans le cadre du cours INF2050
à l'UQAM. Le jeu supporte de 2 à 4 joueurs humains ou robotiques, avec une interface
textuelle en ligne de commande.

Le projet respecte une architecture MVC et inclut des tests unitaires, des tests
de mutation et des tests d'intégration basés sur des joueurs robotiques déterministes.

## Documentation et Téléchargement

La documentation Javadoc et le JAR ce trouve sur la [page web](https://joel-nchuisseu.github.io/skyjo/).

## Instructions d'exécution

### Prérequis
- Java 23 ou supérieur

### Lancer le jeu

```bash
java -jar skyjo.jar nom1 nom2
```


### Joueurs robotiques

Utilisez les noms réservés suivants pour remplacer un joueur par un robot :

| Nom      | Niveau        |
|----------|---------------|
| `Keksli` | Débutant      |
| `MadMax` | Intermédiaire |
| `S38`    | Expert        |

Exemple avec un humain contre un robot :

```bash
java -jar skyjo.jar nom1 Keksli
```

Exemple avec quatre robots :

```bash
java -jar skyjo.jar Keksli MadMax Keksli MadMax
```
Exemple avec une seed :

```bash
java -jar Skyjo.jar 5 nom1 nom2 nom3 nom4
```

## Contributeurs

- Joël Stéphane Tchiengang Nchuisseu
- Hasmik Tadevosyan