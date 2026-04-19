# Skyjo

## Contexte du projet

Skyjo est un jeu de cartes multijoueur implémenté en Java dans le cadre du cours INF2050
à l'UQAM. Le jeu supporte de 2 à 4 joueurs humains ou robotiques, avec une interface
textuelle en ligne de commande.

Le projet respecte une architecture MVC et inclut des tests unitaires, des tests
de mutation et des tests d'intégration basés sur des joueurs robotiques déterministes.

## Documentation et Téléchargement

La documentation javadoc et le jar ce trouve sur la [page web](https://joel-nchuisseu.github.io/skyjo/).

## Instructions d'exécution

### Prérequis
- Java 23 ou supérieur

### Lancer le jeu

```bash
java -jar skyjo.jar Max Ryan
```

Remplacez `Max` et `Ryan` par les noms des joueurs (2 à 4 joueurs supportés).

### Joueurs robotiques

Utilisez les noms réservés suivants pour remplacer un joueur par un robot :

| Nom      | Comportement                          |
|----------|---------------------------------------|
| `Keksli` | Choisit toujours la première option   |
| `MadMax` | Choisit une option aléatoire          |

Exemple avec un humain contre un robot :

```bash
java -jar skyjo.jar Max Keksli
```

Exemple avec quatre robots :

```bash
java -jar skyjo.jar Keksli MadMax Keksli MadMax
```

## Contributeurs

- [Joël Stéphane Tchiengang Nchuisseu](mailto:joel.nchuisseu@gmail.com)
- Hasmik Tadevosyan