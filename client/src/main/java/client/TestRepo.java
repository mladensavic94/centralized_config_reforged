package client;

import org.springframework.data.repository.CrudRepository;

public interface TestRepo extends CrudRepository<Test, Integer> {
}
