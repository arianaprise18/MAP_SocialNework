package domain.validation;


import domain.Prietenie;

/**
 * Validator specific pentru relatia de prietenie intre conturi de Gmail.
 */
public class PrietenieValidator implements Validator<Prietenie> {

    @Override
    public void validate(Prietenie entity) throws ValidationException {
        if (entity == null) {
            throw new ValidationException("Prietenia nu poate fi nula!");
        }
        if (entity.getId() == null || entity.getIdUser1() == null || entity.getIdUser2() == null) {
            throw new ValidationException("Identificatorii utilizatorilor nu pot fi null!");
        }
        if (entity.getIdUser1().equalsIgnoreCase(entity.getIdUser2())) {
            throw new ValidationException("Un utilizator nu se poate imprieteni cu el insusi!");
        }
    }
}