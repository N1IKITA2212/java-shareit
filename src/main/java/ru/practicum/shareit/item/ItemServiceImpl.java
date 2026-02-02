package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.AccessViolationException;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final ItemMapper itemMapper;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

    @Override
    public ItemWithBookingDto getItemById(Long itemId, Long ownerId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
        LocalDateTime now = LocalDateTime.now();
        List<CommentDto> itemCommentsDto = commentRepository.findByItemId(itemId).stream()
                .map(commentMapper::toCommentDto)
                .toList();
        if (item.getOwner().getId().equals(ownerId)) {
            List<Booking> itemBookings = bookingRepository.findByItemIdAndStatus(itemId, BookingStatus.APPROVED);
            BookingDto last = itemBookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(now))
                    .max(Comparator.comparing(Booking::getEnd))
                    .map(bookingMapper::toBookingDto)
                    .orElse(null);
            BookingDto next = itemBookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .min(Comparator.comparing(Booking::getStart))
                    .map(bookingMapper::toBookingDto)
                    .orElse(null);
            return itemMapper.toItemWithBookingDto(item, itemCommentsDto, last, next);
        } else {
            return itemMapper.toItemWithBookingDto(item, itemCommentsDto, null, null);
        }
    }

    @Override
    public List<ItemWithBookingDto> getUserItems(Long ownerId) {
        if (userRepository.findById(ownerId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        LocalDateTime now = LocalDateTime.now();
        List<ItemWithBookingDto> result = new ArrayList<>();
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        List<Booking> bookings = bookingRepository.findByItemOwnerIdAndStatus(ownerId, BookingStatus.APPROVED);
        List<Comment> comments = commentRepository.findByItemOwnerId(ownerId);
        Map<Long, List<Comment>> commentsByItem = comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId()));
        Map<Long, List<Booking>> bookingsByItem = bookings.stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        for (Item item : items) {
            List<Booking> itemBookings = bookingsByItem.getOrDefault(item.getId(), List.of());
            List<CommentDto> itemCommentsDto = commentsByItem.getOrDefault(item.getId(), List.of())
                    .stream()
                    .map(commentMapper::toCommentDto)
                    .toList();
            BookingDto last = itemBookings.stream()
                    .filter(booking -> booking.getEnd().isBefore(now))
                    .max(Comparator.comparing(Booking::getEnd))
                    .map(bookingMapper::toBookingDto)
                    .orElse(null);
            BookingDto next = itemBookings.stream()
                    .filter(booking -> booking.getStart().isAfter(now))
                    .min(Comparator.comparing(Booking::getStart))
                    .map(bookingMapper::toBookingDto)
                    .orElse(null);
            result.add(itemMapper.toItemWithBookingDto(item, itemCommentsDto, last, next));
        }

        return result;
    }

    @Override
    @Transactional
    public ItemDto createItem(Long ownerId, ItemCreateDto itemCreateDto) {
        User owner = userRepository.findById(ownerId).orElseThrow(()
                -> new NotFoundException("Пользователь с id=" + ownerId + " не найден"));
        Item item = itemMapper.fromCreateDto(itemCreateDto);
        item.setOwner(owner);
        return itemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(Long ownerId, ItemPatchDto itemPatchDto, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Предмет с таким id не найден"));
        if (!item.getOwner().getId().equals(ownerId)) {
            throw new AccessViolationException("Редактирование данного предмета недоступно");
        }
        itemMapper.applyPatch(itemPatchDto, item);
        return itemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.findByIsAvailableTrueAndNameContainingIgnoreCaseOrIsAvailableTrueAndDescriptionContainingIgnoreCase(text, text)
                .stream().map(itemMapper::toItemDto).toList();
    }


    @Override
    @Transactional
    public CommentDto createComment(CommentCreateDto commentCreateDto, Long authorId, Long itemId) {
        List<Booking> bookings = bookingRepository.findByItemId(itemId).stream()
                .filter(booking -> booking.getEnd().isBefore(LocalDateTime.now()))
                .filter(booking -> booking.getBooker().getId().equals(authorId))
                .toList();
        if (bookings.isEmpty()) {
            throw new BadRequestException("Комментарий можно оставить только если пользователь бран вещь в аренду");
        }
        Comment comment = commentMapper.fromCommentCreateDto(commentCreateDto);
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(userRepository.findById(authorId).orElseThrow(()
                -> new NotFoundException("Пользователь с id=" + authorId + " не найден")));
        comment.setItem(itemRepository.findById(itemId).orElseThrow(()
                -> new NotFoundException("Предмет с id=" + itemId + " не найден")));
        return commentMapper.toCommentDto(commentRepository.save(comment));

    }
}
