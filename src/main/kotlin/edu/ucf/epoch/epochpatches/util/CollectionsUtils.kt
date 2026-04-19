package edu.ucf.epoch.epochpatches.util

inline fun <K, V> Sequence<Pair<K, V>>.toMultiMap(keyTransform: (K) -> K = {it}, valueTransform: (V) -> V = {it}): Map<K, List<V>> {
    return this.groupBy({ (k, _) -> keyTransform(k) }, { (_, v) -> valueTransform(v) })
}

inline fun <L, R, L2> Sequence<Pair<L, R>>.mapLeft(crossinline transform: (L) -> L2): Sequence<Pair<L2, R>> {
    return this.map { (l, r) -> transform(l) to r }
}

//TODO make a proper set of like zipwith zipby mapleft mapright utils for all this stuff