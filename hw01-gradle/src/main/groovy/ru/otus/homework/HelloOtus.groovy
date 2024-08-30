package ru.otus.homework

import com.google.common.base.MoreObjects

class HelloOtus {
    String name
    String surname
    String email

    HelloOtus(String name, String surname) {
        this.name = name
        this.surname = surname
    }

    void setEmail(String name, String surname, Email mask) {
        this.email = "${name.toLowerCase()}" +
                     "${surname.toLowerCase()}" +
                     "${createEmail(mask)}"
    }

    static final String createEmail(Email mask) {
        def result
        switch (mask) {
            case Email.MAIL: {
                result = '@' + Email.MAIL.name().toLowerCase() + '.ru'
                break
            }
            case Email.YANDEX: {
                result = '@' + Email.YANDEX.name().toLowerCase() + '.ru'
                break
            }
            case Email.GOOGLE: {
                result = '@' + Email.GOOGLE.name().toLowerCase() + '.com'
                break
            }
        }
        return result
    }

    @Override
    String toString() {
        return MoreObjects.toStringHelper(HelloOtus.class)
                .add("name", name)
                .add("surname", surname)
                .add("email", email)
                .toString()
    }
}