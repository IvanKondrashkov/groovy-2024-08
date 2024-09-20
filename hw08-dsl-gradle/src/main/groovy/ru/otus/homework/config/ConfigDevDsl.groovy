package ru.otus.homework.config

class ConfigDevDsl extends ConfigBaseDsl {

    @Override
    def profile(@DelegatesTo(value = ConfigDevDsl) Closure cl) {
        cl.setDelegate(this)
        cl.setResolveStrategy(Closure.DELEGATE_ONLY)
        cl.call()
        mappings(mappings)
    }
}