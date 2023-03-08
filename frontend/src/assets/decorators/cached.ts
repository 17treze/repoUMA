import { shareReplay } from "rxjs/operators";
import { Observable } from "rxjs";

export class Cache {
    static cache = new Map<string, CacheItem>();
    static lock = false;

    static clear() {
        if (!Cache.lock) {
            Cache.lock = true;
            try {
                Cache.cache.clear();
            } finally {
                Cache.lock = false;
            }
        }
    }

    static cleanExpiredItems() {
        if (!Cache.lock) {
            Cache.lock = true;
            try {
                let removeKeys = new Array<string>();
                Cache.cache.forEach((v: CacheItem, key: string) => {
                    if (v.expired()) {
                        removeKeys.push(key);
                    }
                });
                removeKeys.forEach((k) => { Cache.cache.delete(k); });
            } finally {
                Cache.lock = false;
            }
        }
    }
}

export class CacheItem {
    constructor(public cache: Observable<any>, public timestamp: number, public expirationTimeMs: number) {
    }
    expired(): boolean {
        return Date.now() - this.timestamp > this.expirationTimeMs;
    }
}

export function cacheOf(source: () => Observable<any>, key: string, expirationTime: number = 10000): Observable<any> {
    let cacheentry: CacheItem;
    
    Cache.cleanExpiredItems();
    if (!Cache.cache.get(key) || (Cache.cache.get(key).expired())) {
        cacheentry = new CacheItem(source().pipe(shareReplay(1)), Date.now(), expirationTime);
        // cacheentry = new CacheItem(source().pipe(shareReplay(1)), Date.now(), expirationTime);
        /* rxjs 6.4
        let conf: ShareReplayConfig = {
            bufferSize: 1,
            refCount: true
        };
        cacheentry = new CacheItem(source().pipe(shareReplay(conf)), Date.now(), expirationTime);*/
        Cache.cache.set(key, cacheentry);
    } else {
        cacheentry = Cache.cache.get(key);
    }
    return cacheentry.cache;
}

export function Cached(expirationTimeMs = 10000) {
    return function (target, name, descriptor) {
        var originalMethod = descriptor.value;
        descriptor.value = function () {
            var args = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                args[_i - 0] = arguments[_i];
            }
            var instance = this;
            return cacheOf(() => {
                return originalMethod.apply(instance, args);
            }, JSON.stringify([name].concat(arguments)).replace(/\"/gi, ''), expirationTimeMs);
        };
    };
}

export function ClearCache() {
    return function (target, name, descriptor) {
        var originalMethod = descriptor.value;

        descriptor.value = function () {
            var args = [];
            for (var _i = 0; _i < arguments.length; _i++) {
                args[_i - 0] = arguments[_i];
            }
            var instance = this;
            Cache.clear();
            return originalMethod.apply(instance, args);
        };
    };
}