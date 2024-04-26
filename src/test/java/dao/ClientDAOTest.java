package dao;

import model.Client;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientDAOTest {

    private final ClientDAO clientDAO = new ClientDAO();

    @Test
    void findByUsernameTestShouldReturnValue() {
        Client foundClient = clientDAO.findByUsername("moldi");

        assertNotNull(foundClient);
        assertEquals(foundClient.getUsername(), "moldi");
        assertTrue(foundClient.getClientId() > 0);
    }

    @Test
    void updateByUsernameTestShouldReturnValue() {
        Client updatedClient = new Client("test client", "test client", null, null, null, null, null);

        clientDAO.updateByUsername("moldi", updatedClient);
        Client foundClient = clientDAO.findById(1L);

        assertEquals("test client", foundClient.getFirstName());
    }

    @Test
    void deleteByUsernameTestShouldReturnValueGreaterThan0() {
        assertTrue(clientDAO.deleteByUsername("moldi") > 0);
    }
}