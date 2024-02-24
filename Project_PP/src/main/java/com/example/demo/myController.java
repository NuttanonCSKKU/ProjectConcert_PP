package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
public class myController {

    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private MemberRepository memberRepository;

 
    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }
 
    @GetMapping("/Artist")
    public String listArtists(Model model) {
        Iterable<Artist> artists = artistRepository.findAll();
        List<Album> albums = (List<Album>) albumRepository.findAll();
        model.addAttribute("albums", albums);
        model.addAttribute("artists", artists);
        return "Artist"; 
    }
    @GetMapping("/Album")
    public String listAlbum(Model model) {
    	Iterable<Artist> artists = artistRepository.findAll();
        List<Album> albums = (List<Album>) albumRepository.findAll();
        model.addAttribute("albums", albums);
        model.addAttribute("artists", artists);
        return "Album"; 
    }

    @GetMapping("/contact")
    public String getContact(Model model) {
    	  List<Address> addresses = (List<Address>) addressRepository.findAll();
    	  List<Member> members = (List<Member>) memberRepository.findAll();
    	  model.addAttribute("addresses", addresses);
    	  model.addAttribute("members", members);
        return "contact";
    }
    
    @GetMapping("/edit/{memberId}")
    public String editMemberForm(@PathVariable int memberId, Model model) {
        Member member = memberRepository.findById(memberId).orElse(null);
        model.addAttribute("member", member);
        return "editMember";
    }

    @PostMapping("/edit/{memberId}")
    public String editMemberUpdate(
            @PathVariable("memberId") int memberId,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("address_line1") String address_line1,
            @ModelAttribute("updatedMember") Member updatedMember,
            RedirectAttributes redirectAttributes) {
        Member member = memberRepository.findById(memberId).orElse(null);

        if (member == null) {
            return "redirect:/";
        }
        
        Address addressEntity = member.getAddress();
        addressEntity.setZipcode(zipcode);
        addressEntity.setAddress_line1(address_line1);

        // Update member's properties
        member.setName(updatedMember.getName());
        member.setNumber(updatedMember.getNumber());
        member.setTelephone(updatedMember.getTelephone());
        member.setEmail(updatedMember.getEmail());

        // Check if updatedMember has a non-null Address
        if (updatedMember.getAddress() != null) {
            Address updatedAddress = updatedMember.getAddress();

            // Check if updatedAddress's zipcode is not null
            if (updatedAddress.getZipcode() != null) {
                member.getAddress().setZipcode(updatedAddress.getZipcode());
            }

            // Check if updatedAddress's address_line1 is not null
            if (updatedAddress.getAddress_line1() != null) {
                member.getAddress().setAddress_line1(updatedAddress.getAddress_line1());
            }
        }

        // Save the updated member
        memberRepository.save(member);

        redirectAttributes.addFlashAttribute("successMessageMemberEdit", "แก้ไขข้อมูลสมาชิกสำเร็จแล้ว");
        return "redirect:/contact";
    }

    @PostMapping("/createArtist")
    public String createArtist(@ModelAttribute Artist artist, @RequestParam("image_name") MultipartFile imageFile,
                               RedirectAttributes redirectAttributes, Model model) {
        String imageName = imageFile.getOriginalFilename();
        
        artist.setImageName(imageName);
        if (!imageFile.isEmpty()) {
            try {
                byte[] bytes = imageFile.getBytes();
                Path path = Paths.get("src/main/resources/static/image/" + imageName);
                Files.write(path, bytes);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }
        artistRepository.save(artist);
        redirectAttributes.addFlashAttribute("success", "Artist created successfully.");
        return "redirect:/Artist";
    }
    
    @PostMapping("/createAlbum")
    public String createAlbum(@RequestParam("name") String albumName, @RequestParam("artist") Artist artistId, Model model) {
        // ตรวจสอบว่า artistId ไม่เป็นค่าว่าง
        if (artistId != null) {
            // สร้างอัลบั้มใหม่และกำหนดชื่อ
            Album album = new Album();
            album.setAlbumname(albumName);
            // สร้างอัลบั้มและผูกกับศิลปินโดยใช้ ID ของศิลปิน
            album.setArtist(artistId);
            // บันทึกอัลบั้มลงในฐานข้อมูล
            albumRepository.save(album);
            // ส่งข้อมูลศิลปินและอัลบั้มทั้งหมดไปยัง view
            List<Artist> artists = (List<Artist>) artistRepository.findAll();
            List<Album> albums = (List<Album>) albumRepository.findAll();
            model.addAttribute("artists", artists);
            model.addAttribute("albums", albums);
            return "redirect:/Album";
        } else {
            // กรณีที่ไม่ได้เลือกศิลปิน
            return "redirect:/Artist"; 
        }
    }
    @PostMapping("/createMember")
    public String createMember(@RequestParam String name, @RequestParam String telephone, 
                               @RequestParam String number, @RequestParam String email, 
                               @RequestParam String zipcode, @RequestParam String address_line1) {
        Member newMember = new Member();
        newMember.setName(name);
        newMember.setTelephone(telephone);
        newMember.setNumber(number);
        newMember.setEmail(email);

        Address newAddress = new Address();
        newAddress.setZipcode(zipcode);
        newAddress.setAddress_line1(address_line1);

        // Set the address in the member
        newMember.setAddress(newAddress);

        // Save the member (and the associated address) to the database
        memberRepository.save(newMember);

        return "redirect:/contact";
        }
        
    @PostMapping("/artist/delete/{artistId}")
    public String deleteArtist(@PathVariable("artistId") int artistId, RedirectAttributes redirectAttributes) {
        // Find the artist you want to delete
        Optional<Artist> optionalArtist = artistRepository.findById(artistId);
        if (optionalArtist.isPresent()) {
            Artist artist = optionalArtist.get();

            // Delete the artist's image 
            String imageName = artist.getImageName();
            if (imageName != null && !imageName.isEmpty()) {
                Path imagePath = Paths.get("src/main/resources/static/image/" + imageName);
                if (Files.exists(imagePath)) {
                    try {
                        Files.delete(imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Handle the exception appropriately
                    }
                }
            }

         // Delete the album
            albumRepository.deleteAll(artist.getAlbums());
         // Delete the artist
            artistRepository.delete(artist);
            redirectAttributes.addFlashAttribute("success", "Artist deleted successfully.");
        }
        return "redirect:/Artist";
    }
    
      
    @PostMapping("/album/deleteAlbum/{albumId}")
    public String deleteAlbum(@PathVariable Integer albumId) {
        albumRepository.deleteById(albumId);
        return "redirect:/Album"; // เปลี่ยนเส้นทาง URL ตามที่คุณต้องการให้ไปหลังจากลบเสร็จสมบูรณ์
    }
    
    @PostMapping("/member/deleteMember/{memberId}")
    public String deleteMember(@PathVariable("memberId") int memberId, RedirectAttributes redirectAttributes) {
        // Find the member you want to delete
        Member member = memberRepository.findById(memberId).orElse(null);
        if (member == null) {
            // Handle the case where the member is not found
            redirectAttributes.addFlashAttribute("error", "Member not found.");
        } else {
            // Check if the member has an associated address
            Address address = member.getAddress();
            if (address != null) {
                member.setAddress(null);
                memberRepository.save(member);
                memberRepository.delete(member);
                addressRepository.delete(address);
            } else {
                memberRepository.delete(member);
            }
            redirectAttributes.addFlashAttribute("success", "Member deleted successfully.");
        }
        return "redirect:/contact";
    }


 }

    

