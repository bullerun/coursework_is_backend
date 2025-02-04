package coursework.backend.controller;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/tenders")
public class TenderController {

//    private final TenderService tenderService;
//
//    public TenderController(TenderService tenderService) {
//        this.tenderService = tenderService;
//    }
//
//    @GetMapping("/ping")
//    public ResponseEntity<String> ping() {
//        return ResponseEntity.ok("ok");
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Tender>> getTenders(@RequestParam(required = false) List<String> serviceType) {
//        return ResponseEntity.ok(tenderService.getTenders(serviceType));
//    }
//
//    @PostMapping("/new")
//    public ResponseEntity<Tender> createTender(@RequestBody TenderRequest request) {
//        return ResponseEntity.ok(tenderService.createTender(request));
//    }
//
//    @GetMapping("/my")
//    public ResponseEntity<List<Tender>> getUserTenders(@RequestParam String username) {
//        return ResponseEntity.ok(tenderService.getUserTenders(username));
//    }
//
//    @GetMapping("/{tenderId}/status")
//    public ResponseEntity<String> getTenderStatus(@PathVariable Long tenderId) {
//        return ResponseEntity.ok(tenderService.getTenderStatus(tenderId));
//    }
//
//    @PutMapping("/{tenderId}/status")
//    public ResponseEntity<Tender> updateTenderStatus(@PathVariable Long tenderId, @RequestParam String status) {
//        return ResponseEntity.ok(tenderService.updateTenderStatus(tenderId, status));
//    }
//
//    @PatchMapping("/{tenderId}/edit")
//    public ResponseEntity<Tender> editTender(@PathVariable Long tenderId, @RequestBody TenderRequest request) {
//        return ResponseEntity.ok(tenderService.editTender(tenderId, request));
//    }
//
//    @PutMapping("/{tenderId}/rollback/{version}")
//    public ResponseEntity<Tender> rollbackTender(@PathVariable Long tenderId, @PathVariable int version) {
//        return ResponseEntity.ok(tenderService.rollbackTender(tenderId, version));
//    }
}
